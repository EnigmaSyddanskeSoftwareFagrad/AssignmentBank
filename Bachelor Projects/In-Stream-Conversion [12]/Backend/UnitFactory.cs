using Bsc_In_Stream_Conversion.Database;
using Bsc_In_Stream_Conversion.Lookup;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using static Bsc_In_Stream_Conversion.Lookup.Prefixes;

namespace Bsc_In_Stream_Conversion
{
    public class UnitFactory
    {
        private IDatabaseAccess db;

        public UnitFactory(IDatabaseAccess db)
        {
            this.db = db;
        }

        public async Task<UserUnit> Parse(string input)
        {
            var uu = new UserUnit();
            input = input.Replace(" ", "").Replace("\t", "").ToUpperInvariant();

            if (!input.Contains("/") && !input.Contains("*") && !input.Contains("^"))
            {
                uu = await ParseSimpleUnit(input);
            }
            else
            {
                uu = await ParseFractionPart(input.Split("/")[0], uu, true);
                if (input.Contains("/"))
                {
                    uu = await ParseFractionPart(input.Split("/")[1], uu, false);
                } 
            }
            return uu;
        }

        private async Task<UserUnit> ParseSimpleUnit(string input)
        {
            FindPrefix(input, out var hasPrefix, out var prefixFactor);
            if (hasPrefix)
            {
                input = input.Replace(prefixFactor?.Item1, "");
            }
            var dbUnit = await db.SelectUnit(input);

            var uu = new UserUnit(name: input,
                numeratorPrefixes: hasPrefix ? (prefixFactor?.Item2) : ConversionFactor.One,
                denominatorPrefixes: ConversionFactor.One,
                multiplier: dbUnit.ConversionMultiplier,
                offset: dbUnit.ConversionOffset,
                dv: dbUnit.DimensionVector);
            return uu;
        }

        private async Task<UserUnit> ParseFractionPart(string input, UserUnit uu, bool isNumerator)
        {
            var parts = input.Split("*");
            foreach (var part in parts)
            {
                FindPrefix(part, out var hasPrefix, out var prefixFactor);

                if (part.Contains("^"))
                {
                    return await SeperatePowers(uu, part, hasPrefix, prefixFactor, isNumerator);
                }
                else
                {
                    string partToAdd = part;
                    if (hasPrefix)
                    {
                        partToAdd = part.Replace(prefixFactor?.Item1, "");
                    }
                    var dbUnit = await db.SelectUnit(partToAdd);

                    UserUnit newUU = new UserUnit(name: partToAdd,
                                                  numeratorPrefixes:    hasPrefix ? (prefixFactor?.Item2) : ConversionFactor.One,
                                                  denominatorPrefixes:  ConversionFactor.One,
                                                  multiplier:           dbUnit.ConversionMultiplier,
                                                  offset:               0,
                                                  dv:                   dbUnit.DimensionVector);

                    if (isNumerator)
                    {
                        uu *= newUU;
                    }
                    else
                    {
                        uu /= newUU;
                    }
                }
            }

            return uu;
        }

        private async Task<UserUnit> SeperatePowers(UserUnit uu, string part, bool hasPrefix, (string, ConversionFactor)? prefixFactor, bool isNumerator)
        {
            var splitPart = part.Split("^");
            var amount = int.Parse(splitPart[1]);
            if (hasPrefix)
            {
                splitPart[0] = splitPart[0].Replace(prefixFactor?.Item1, "");
            }
            for (int i = 0; i < amount; i++)
            {
                string partToAdd = splitPart[0];
                if (hasPrefix)
                {
                    partToAdd = splitPart[0].Replace(prefixFactor?.Item1, "");
                }
                var dbUnit = await db.SelectUnit(partToAdd);

                UserUnit newUU = new UserUnit(name: partToAdd, 
                                              numeratorPrefixes: hasPrefix ? (prefixFactor?.Item2) : ConversionFactor.One,
                                              denominatorPrefixes: ConversionFactor.One,
                                              multiplier: dbUnit.ConversionMultiplier,
                                              offset: dbUnit.ConversionOffset,
                                              dv: dbUnit.DimensionVector);

                if (isNumerator)
                {
                    uu *= newUU;
                }
                else
                {
                    uu /= newUU;
                }
            }
            return uu;
            throw new InvalidOperationException("Something went wrong in passing the unit");
        }

        private void FindPrefix(string part, out bool hasPrefix, out (string, ConversionFactor)? prefixFactor)
        {
            hasPrefix = false;
            prefixFactor = null;
            foreach (var prefix in Prefixes.IntegerFactor.Keys)
            {
                if (part.StartsWith(prefix) && !String.IsNullOrWhiteSpace(prefix))
                {
                    hasPrefix = true;
                    prefixFactor = (prefix, Prefixes.IntegerFactor[prefix]);
                    break;
                }
            }
        }
    }
}
