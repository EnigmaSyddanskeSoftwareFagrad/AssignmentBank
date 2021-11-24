using Bsc_In_Stream_Conversion.Database;
using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.Globalization;
using System.IO;
using System.Linq;
using System.Net;
using System.Threading.Tasks;
using Bsc_In_Stream_Conversion.Model;

namespace Bsc_In_Stream_Conversion
{
    public class QUDTParser
    {
        public static async Task ReadFileAndStoreInDatabase()
        {
            var url = "https://raw.githubusercontent.com/qudt/qudt-public-repo/master/vocab/unit/VOCAB_QUDT-UNITS-ALL-v2.1.ttl";

            var localPath = Path.GetTempFileName();

            new WebClient().DownloadFile(url, localPath);

            var unitsWithoutMultiplier = new List<Unit>();

            var units = new List<Unit>();
            var currentUnit = new Unit();
            bool skipTillNextDot = false;
            bool isInDescription = false;
            int currentLineOfUnit = 0;

            using (var filestream = new FileStream(localPath, FileMode.Open))
            {
                using (var reader = new StreamReader(filestream))
                {
                    while (reader.ReadLine() != ".") ;

                    while (!reader.EndOfStream)
                    {
                        var line = reader.ReadLine();

                        //Skip empty lines
                        if (string.IsNullOrWhiteSpace(line))
                        {
                            currentLineOfUnit++;
                            continue;
                        }

                        //Dot indicates new element
                        if(line == ".")
                        {
                            if(!skipTillNextDot) units.Add(currentUnit);
                            skipTillNextDot = false;
                            isInDescription = false;
                            currentLineOfUnit = 0;
                            continue;
                        }

                        if (skipTillNextDot) continue;

                        //Is new element a unit or not
                        if (currentLineOfUnit == 0 && !line.StartsWith("unit:"))
                        {
                            skipTillNextDot = true;
                            continue;
                        }else if(currentLineOfUnit == 0 && line.StartsWith("unit:"))
                        {
                            currentUnit = new Unit();
                            currentUnit.SystemName = line.Remove(0, 5);
                            currentLineOfUnit++;
                            continue;
                        }

                        var splitLine = line.Split(" ");

                        //Hopefully this means the description is over
                        if (line.StartsWith("  "))
                        {
                            isInDescription = false;
                        }

                        //Descriptions can have new lines, so we need a mechanism to deal with it
                        if (isInDescription)
                        {
                            currentUnit.Description += " " + line;
                            currentLineOfUnit++;
                            continue;
                        }
                        if(currentUnit.SystemName == "FT") { int i = 42; }

                        //Read the element variable
                        switch (splitLine[2])
                        {
                            case "qudt:conversionMultiplier": 
                                currentUnit.ConversionMultiplier = splitLine[3].Length > 29 ? decimal.Parse(splitLine[3].Remove(28), NumberStyles.AllowDecimalPoint, CultureInfo.InvariantCulture) : decimal.Parse(splitLine[3], NumberStyles.AllowDecimalPoint, CultureInfo.InvariantCulture);
                                break;
                            case "qudt:conversionOffset":
                                currentUnit.ConversionOffset = splitLine[3].Length > 29 ? decimal.Parse(splitLine[3].Remove(28), NumberStyles.AllowDecimalPoint, CultureInfo.InvariantCulture) : decimal.Parse(splitLine[3], NumberStyles.AllowDecimalPoint, CultureInfo.InvariantCulture);
                                break;
                            case "qudt:hasDimensionVector": 
                                currentUnit.DimensionVector = DimensionVector.Parse(splitLine[3].Remove(0, 5));
                                break;
                            case "qudt:symbol": 
                                currentUnit.Symbol = splitLine[3].Replace("\"", "");
                                break;
                            case "rdfs:label": 
                                if (line.Contains("@en-us")) break;
                                currentUnit.UnitName = line.Replace("  rdfs:label \"", "").Replace("\"@en ;", "");
                                break;
                            case "dcterms:description": 
                                currentUnit.Description = line.Replace("  dcterms:description ", "");
                                isInDescription = true;
                                break;
                            case "qudt:plainTextDescription": 
                                currentUnit.Description = line.Replace("  qudt:plainTextDescription ", "");
                                break;
                            case "qudt:hasQuantityKind":
                                currentUnit.QuantityKinds.Add(splitLine[3].Replace("quantitykind:", ""));
                                break;
                        }

                        currentLineOfUnit++;
                    }
                }
            }

            string logging = "";

            units = units.Where(u => !u.QuantityKinds.Contains("Currency")).ToList();

            unitsWithoutMultiplier = units.Where(u => u.ConversionMultiplier == -1).ToList();

            var weirdUnitsAsJson = JsonConvert.SerializeObject(unitsWithoutMultiplier);

            var convertionlessUnits = units.Where(u => u.ConversionMultiplier == 0).ToList();

            units = units.Where(u => u.SystemName.Length != 0 && u.ConversionMultiplier != 0).ToList();

            var db = new DatabaseAccess();
            var currentUnits = new List<Unit>();
            foreach(var unit in units)
            {
                currentUnits.Add(unit);
                if (unit.DimensionVector is null) logging += $"{Environment.NewLine}{unit.SystemName} has no dimension vector";
                if(currentUnits.Count >= 5)
                {
                    logging += Environment.NewLine + "Adding 5 more units. Last unit: " + unit.SystemName;
                    try
                    {
                        await db.InsertUnitsAsync(currentUnits);
                    }catch(Exception e)
                    {
                        Console.Write(e.Message);
                    }
                    currentUnits = new List<Unit>();
                }
            }
            

            File.Delete(localPath);
        }
    }
}
