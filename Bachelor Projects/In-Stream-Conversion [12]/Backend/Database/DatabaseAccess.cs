using Npgsql;
using Serilog;
using System;
using System.Collections.Generic;
using System.Data.Common;
using System.Linq;
using System.Threading;
using System.Threading.Tasks;
using Bsc_In_Stream_Conversion.Model;

namespace Bsc_In_Stream_Conversion.Database
{
    public class DatabaseAccess : IDatabaseAccess
    {//hattie.db.elephantsql.com
        private readonly String connString = "DATABASE CONNECTION STRING REMOVED";

        private Semaphore connectionPool = new Semaphore(5, 5);

        private async Task<NpgsqlConnection> getConnection()
        {
            connectionPool.WaitOne();
            var conn = new NpgsqlConnection(connString);
            await conn.OpenAsync();
            return conn;
        }

        public async Task<int> InsertUnitsAsync(IEnumerable<Unit> units)
        {
            int succesfulInserts = 0;
            foreach (var unit in units)
            {
                if (await InsertUnitAsync(unit))
                {
                    succesfulInserts++;
                }
            }
            return succesfulInserts;
        }

        public async Task<bool> InsertUnitAsync(Unit unit)
        {
            bool wasSuccesful = true;
            wasSuccesful = wasSuccesful && await InsertUnitAsync(unit.UnitName, unit.SystemName, unit.Description, unit.Symbol, unit.ConversionMultiplier, unit.ConversionOffset);
            if (unit.DimensionVector != null)
            {
                wasSuccesful = wasSuccesful && await InsertDimensionVectorAsync(unit.SystemName,
                                                                 unit.DimensionVector.AmountOfSubstance,
                                                                 unit.DimensionVector.ElectricCurrent,
                                                                 unit.DimensionVector.Length,
                                                                 unit.DimensionVector.LuminousIntensity,
                                                                 unit.DimensionVector.Mass,
                                                                 unit.DimensionVector.Temperature,
                                                                 unit.DimensionVector.Time,
                                                                 unit.DimensionVector.Dimensionless);
            }
            foreach (var quantityKind in unit.QuantityKinds)
            {
                wasSuccesful = wasSuccesful && await InsertQuantityKindAsync(unit.SystemName, quantityKind);
            }
            return wasSuccesful;
        }

#nullable enable
        private async Task<bool> InsertUnitAsync(string UnitName, string SystemName, string? Description, string? Symbol, decimal ConversionMultiplier, decimal ConversionOffset)
        {
            if (SystemName.Length == 0 || ConversionMultiplier == 0)
            {
                throw new Exception($"SystemName: {SystemName}, ConversionMultiplier: {ConversionMultiplier}");//return false;
            }


            using NpgsqlConnection conn = await getConnection();

            //INSERT INTO "Units" ("UnitName", "SystemName", "Description", "Symbol", "ConversionMultiplier") VALUES ('Carlos', 'CLS', 'Unit measure of carlos', null, 1)
            await using (var cmd = new NpgsqlCommand("INSERT INTO \"Units\" (\"UnitName\", \"SystemName\", \"Description\", \"Symbol\", \"ConversionMultiplier\", \"ConversionOffset\") VALUES (@UnitName, @SystemName, @Description, @Symbol, @ConversionMultiplier, @ConversionOffset)", conn))
            {
                if (Description == null)
                {
                    cmd.Parameters.AddWithValue("Description", DBNull.Value);
                }
                else
                {
                    cmd.Parameters.AddWithValue("Description", NpgsqlTypes.NpgsqlDbType.Varchar, Description);
                }
                if (Symbol == null)
                {
                    cmd.Parameters.AddWithValue("Symbol", DBNull.Value);

                }
                else
                {
                    cmd.Parameters.AddWithValue("Symbol", NpgsqlTypes.NpgsqlDbType.Varchar, Symbol);
                }
                if (UnitName == null)
                {
                    cmd.Parameters.AddWithValue("UnitName", DBNull.Value);

                }
                else
                {
                    cmd.Parameters.AddWithValue("UnitName", NpgsqlTypes.NpgsqlDbType.Varchar, UnitName);
                }

                cmd.Parameters.AddWithValue("SystemName", NpgsqlTypes.NpgsqlDbType.Varchar, SystemName);
                cmd.Parameters.AddWithValue("ConversionMultiplier", NpgsqlTypes.NpgsqlDbType.Numeric, ConversionMultiplier);
                cmd.Parameters.AddWithValue("ConversionOffset", NpgsqlTypes.NpgsqlDbType.Numeric, ConversionOffset);
                cmd.Prepare();
                try
                {
                    int returnValue = await cmd.ExecuteNonQueryAsync();
                    if (returnValue != 0)
                    {
                        return true;
                    }
                }
                catch (DbException e)
                {
                    Log.Error(e.Message + e.StackTrace);
                    Console.Error.WriteLine("Errro Message: " + e.Message);
                    Console.Error.WriteLine(e.StackTrace);
                    Console.Error.Flush();
                }
            }
            connectionPool.Release();
            return false;
        }

        private async Task<bool> InsertDimensionVectorAsync(string SystemName, short AmountOfSubstance, short ElectricCurrent, short Length, short LuminousIntensity, short Mass, short Temperature, short Time, short Dimensionless)
        {
            if (SystemName.Length == 0)
            {
                return false;
            }

            using NpgsqlConnection conn = await getConnection();



            //INSERT INTO "Units" ("UnitName", "SystemName", "Description", "Symbol", "ConversionMultiplier") VALUES ('Carlos', 'CLS', 'Unit measure of carlos', null, 1)
            await using (var cmd = new NpgsqlCommand("INSERT INTO \"DimensionVectors\" (\"SystemName\", \"AmountOfSubstance\", \"ElectricCurrent\", \"Length\", \"LuminousIntensity\", \"Mass\", \"Temperature\", \"Time\", \"Dimensionless\") VALUES (@SystemName, @AmountOfSubstance, @ElectricCurrent, @Length, @LuminousIntensity, @Mass, @Temperature, @Time, @Dimensionless)", conn))
            {
                cmd.Parameters.AddWithValue("SystemName", NpgsqlTypes.NpgsqlDbType.Varchar, SystemName);
                cmd.Parameters.AddWithValue("AmountOfSubstance", NpgsqlTypes.NpgsqlDbType.Smallint, AmountOfSubstance);
                cmd.Parameters.AddWithValue("ElectricCurrent", NpgsqlTypes.NpgsqlDbType.Smallint, ElectricCurrent);
                cmd.Parameters.AddWithValue("Length", NpgsqlTypes.NpgsqlDbType.Smallint, Length);
                cmd.Parameters.AddWithValue("LuminousIntensity", NpgsqlTypes.NpgsqlDbType.Smallint, LuminousIntensity);
                cmd.Parameters.AddWithValue("Mass", NpgsqlTypes.NpgsqlDbType.Smallint, Mass);
                cmd.Parameters.AddWithValue("Temperature", NpgsqlTypes.NpgsqlDbType.Smallint, Temperature);
                cmd.Parameters.AddWithValue("Time", NpgsqlTypes.NpgsqlDbType.Smallint, Time);
                cmd.Parameters.AddWithValue("Dimensionless", NpgsqlTypes.NpgsqlDbType.Smallint, Dimensionless);
                cmd.Prepare();
                try
                {
                    int returnValue = await cmd.ExecuteNonQueryAsync();
                    if (returnValue != 0)
                    {
                        return true;
                    }
                }
                catch (DbException e)
                {
                    Console.Error.WriteLine("Errro Message: " + e.Message);
                    Console.Error.WriteLine(e.StackTrace);
                    Console.Error.Flush();
                }
            }
            connectionPool.Release();
            return false;
        }

        private async Task<bool> InsertQuantityKindAsync(string SystemName, string Name)
        {
            if (Name.Length == 0 || SystemName.Length == 0)
            {
                return false;
            }
            using NpgsqlConnection conn = await getConnection();

            //INSERT INTO "Units" ("UnitName", "SystemName", "Description", "Symbol", "ConversionMultiplier") VALUES ('Carlos', 'CLS', 'Unit measure of carlos', null, 1)
            await using (var cmd = new NpgsqlCommand("INSERT INTO \"QuantityKind\" (\"SystemName\", \"Name\") VALUES (@SystemName, @Name)", conn))
            {
                cmd.Parameters.AddWithValue("SystemName", NpgsqlTypes.NpgsqlDbType.Varchar, SystemName);
                cmd.Parameters.AddWithValue("Name", NpgsqlTypes.NpgsqlDbType.Varchar, Name);
                cmd.Prepare();
                try
                {
                    int returnValue = await cmd.ExecuteNonQueryAsync();
                    if (returnValue != 0)
                    {
                        return true;
                    }
                }
                catch (DbException e)
                {
                    Console.Error.WriteLine("Errro Message: " + e.Message);
                    Console.Error.WriteLine(e.StackTrace);
                    Console.Error.Flush();
                }
            }
            connectionPool.Release();
            return false;
        }

        public async Task<Unit> SelectUnit(string SystemName)
        {
            if (SystemName == null) throw new ArgumentNullException("SystemName Cannot be null");
            if (SystemName.Length == 0) throw new ArgumentException("SystemName Cannot be empty");

            try
            {
                using var connection = await getConnection();
                Unit unit = new Unit();
                await using (var cmd = new NpgsqlCommand("SELECT * FROM \"Units\" WHERE \"SystemName\" ILIKE @SystemName ", connection))
                {
                    cmd.Parameters.AddWithValue("SystemName", NpgsqlTypes.NpgsqlDbType.Varchar, SystemName);
                    try
                    {
                        int i = 42;
                        using NpgsqlDataReader reader = cmd.ExecuteReader();
                        reader.Read();
                        unit.UnitName = reader.GetString(reader.GetOrdinal("UnitName"));
                        unit.Description = reader.GetString(reader.GetOrdinal("Description"));
                        unit.SystemName = reader.GetString(reader.GetOrdinal("SystemName"));
                        unit.ConversionMultiplier = reader.GetDecimal(reader.GetOrdinal("ConversionMultiplier"));
                        //unit.Symbol = reader.GetString(reader.GetOrdinal("Symbol"));  Not needed so far throws exception when null
                        unit.ConversionOffset = reader.GetDecimal(reader.GetOrdinal("ConversionOffset"));
                    }
                    catch (DbException e)
                    {
                        Console.Error.WriteLine("Errro Message: " + e.Message);
                        Console.Error.WriteLine(e.StackTrace);
                        Console.Error.Flush();
                    }
                    catch (InvalidOperationException e)
                    {
                        throw new InvalidOperationException(e.Message + " SystemName: " + SystemName);
                    }
                }
                DimensionVector dimensionVector = new DimensionVector();
                unit.DimensionVector = dimensionVector;
                await using (var cmd = new NpgsqlCommand("SELECT * FROM \"DimensionVectors\" WHERE \"SystemName\" ILIKE @SystemName ", connection))
                {
                    cmd.Parameters.AddWithValue("SystemName", NpgsqlTypes.NpgsqlDbType.Varchar, SystemName);
                    try
                    {
                        using NpgsqlDataReader reader = cmd.ExecuteReader();
                        reader.Read();
                        dimensionVector.AmountOfSubstance = reader.GetInt16(reader.GetOrdinal("AmountOfSubstance"));
                        dimensionVector.Dimensionless = reader.GetInt16(reader.GetOrdinal("Dimensionless"));
                        dimensionVector.ElectricCurrent = reader.GetInt16(reader.GetOrdinal("ElectricCurrent"));
                        dimensionVector.Length = reader.GetInt16(reader.GetOrdinal("Length"));
                        dimensionVector.LuminousIntensity = reader.GetInt16(reader.GetOrdinal("LuminousIntensity"));
                        dimensionVector.Mass = reader.GetInt16(reader.GetOrdinal("Mass"));
                        dimensionVector.Temperature = reader.GetInt16(reader.GetOrdinal("Temperature"));
                        dimensionVector.Time = reader.GetInt16(reader.GetOrdinal("Time"));
                    }
                    catch (DbException e)
                    {
                        Log.Error(e.Message + e.StackTrace);
                        Console.Error.WriteLine("Errro Message: " + e.Message);
                        Console.Error.WriteLine(e.StackTrace);
                        Console.Error.Flush();
                    }catch (InvalidOperationException e)
                    {
                        throw new InvalidOperationException(e.Message + " SystemName: " + SystemName);
                    }
                }
                List<string> quantityKinds = new List<string>();
                unit.QuantityKinds = quantityKinds;
                await using (var cmd = new NpgsqlCommand("SELECT * FROM \"QuantityKind\" WHERE \"SystemName\" ILIKE @SystemName ", connection))
                {
                    cmd.Parameters.AddWithValue("SystemName", NpgsqlTypes.NpgsqlDbType.Varchar, SystemName);
                    try
                    {
                        using NpgsqlDataReader reader = cmd.ExecuteReader();
                        while (reader.Read())
                        {
                            quantityKinds.Add(reader.GetString(reader.GetOrdinal("Name")));
                        }
                    }
                    catch (DbException e)
                    {
                        Log.Error(e.Message + e.StackTrace);
                        Console.Error.WriteLine("Errro Message: " + e.Message);
                        Console.Error.WriteLine(e.StackTrace);
                        Console.Error.Flush();
                    }catch (InvalidOperationException e)
                    {
                        throw new InvalidOperationException(e.Message + " SystemName: " + SystemName);
                    }
                }
                return unit;
            }
            catch (DbException e)
            {
                Log.Error(e.Message + e.StackTrace);
                Console.Error.WriteLine(e.Message + Environment.NewLine + e.StackTrace);
            }
            finally
            {
                connectionPool.Release();
            }
            throw new Exception("Something Unexpected happened");
        }

        public async Task<List<Unit>> SelectUnitByUnitName(string UnitName)
        {
            if (UnitName == null) throw new ArgumentNullException("UnitName Cannot be null");
            if (UnitName.Length == 0) throw new ArgumentException("UnitName Cannot be empty");

            UnitName = "%" + UnitName + "%";

            try
            {
                using var connection = await getConnection();
                List<Unit> unitList = new List<Unit>();
                //TODO: Better query: select * from "Units" join "DimensionVectors" DV on "Units"."SystemName" = DV."SystemName" join "QuantityKind" QK on "Units"."SystemName" = QK."SystemName" where "Units"."UnitName" ilike
                await using (var cmd = new NpgsqlCommand("SELECT * FROM \"Units\" WHERE \"UnitName\" ilike @UnitName ", connection))
                {
                    cmd.Parameters.AddWithValue("UnitName", NpgsqlTypes.NpgsqlDbType.Varchar, UnitName);
                    try
                    {
                        using NpgsqlDataReader reader = cmd.ExecuteReader();
                        while (reader.Read())
                        {
                            var unit = new Unit();
                            unit.UnitName = reader.GetString(reader.GetOrdinal("UnitName"));
                            unit.Description = reader.GetString(reader.GetOrdinal("Description"));
                            unit.SystemName = reader.GetString(reader.GetOrdinal("SystemName"));
                            unit.ConversionMultiplier = reader.GetDecimal(reader.GetOrdinal("ConversionMultiplier"));
                            //unit.Symbol = reader.GetString(reader.GetOrdinal("Symbol"));  Not needed so far throws exception when null
                            unit.ConversionOffset = reader.GetDecimal(reader.GetOrdinal("ConversionOffset"));
                            unitList.Add(unit);
                        }
                    }
                    catch (DbException e)
                    {
                        Console.Error.WriteLine("Errro Message: " + e.Message);
                        Console.Error.WriteLine(e.StackTrace);
                        Console.Error.Flush();
                    }
                }
                foreach (var unit in unitList)
                {
                    DimensionVector dimensionVector = new DimensionVector();
                    unit.DimensionVector = dimensionVector;
                    await using (var cmd = new NpgsqlCommand("SELECT * FROM \"DimensionVectors\" WHERE \"SystemName\" = @SystemName ", connection))
                    {
                        cmd.Parameters.AddWithValue("SystemName", NpgsqlTypes.NpgsqlDbType.Varchar, unit.SystemName);
                        try
                        {
                            using NpgsqlDataReader reader = cmd.ExecuteReader();
                            if (reader.Read())
                            {
                                dimensionVector.AmountOfSubstance = reader.GetInt16(reader.GetOrdinal("AmountOfSubstance"));
                                dimensionVector.Dimensionless = reader.GetInt16(reader.GetOrdinal("Dimensionless"));
                                dimensionVector.ElectricCurrent = reader.GetInt16(reader.GetOrdinal("ElectricCurrent"));
                                dimensionVector.Length = reader.GetInt16(reader.GetOrdinal("Length"));
                                dimensionVector.LuminousIntensity = reader.GetInt16(reader.GetOrdinal("LuminousIntensity"));
                                dimensionVector.Mass = reader.GetInt16(reader.GetOrdinal("Mass"));
                                dimensionVector.Temperature = reader.GetInt16(reader.GetOrdinal("Temperature"));
                                dimensionVector.Time = reader.GetInt16(reader.GetOrdinal("Time"));
                            }
                        }
                        catch (DbException e)
                        {
                            Log.Error(e.Message + e.StackTrace);
                            Console.Error.WriteLine("Errro Message: " + e.Message);
                            Console.Error.WriteLine(e.StackTrace);
                            Console.Error.Flush();
                        }
                    }
                    List<string> quantityKinds = new List<string>();
                    unit.QuantityKinds = quantityKinds;
                    await using (var cmd = new NpgsqlCommand("SELECT * FROM \"QuantityKind\" WHERE \"SystemName\" = @SystemName ", connection))
                    {
                        cmd.Parameters.AddWithValue("SystemName", NpgsqlTypes.NpgsqlDbType.Varchar, unit.SystemName);
                        try
                        {
                            using NpgsqlDataReader reader = cmd.ExecuteReader();
                            while (reader.Read())
                            {
                                quantityKinds.Add(reader.GetString(reader.GetOrdinal("Name")));
                            }
                        }
                        catch (DbException e)
                        {
                            Log.Error(e.Message + e.StackTrace);
                            Console.Error.WriteLine("Errro Message: " + e.Message);
                            Console.Error.WriteLine(e.StackTrace);
                            Console.Error.Flush();
                        }
                    }
                }
                return unitList;
            }
            catch (DbException e)
            {
                Log.Error(e.Message + e.StackTrace);
                Console.Error.WriteLine(e.Message + Environment.NewLine + e.StackTrace);
            }
            finally
            {
                connectionPool.Release();
            }
            throw new Exception("Something Unexpected happened");
        }
    }
}
