using Bsc_In_Stream_Conversion;
using Bsc_In_Stream_Conversion.Model;
using static Bsc_In_Stream_Conversion.Lookup.Prefixes;

namespace XunitTests.Mocks
{
    public class MockUnits
    {
        //Length
        public static UserUnit Meter = new UserUnit("M", new ConversionFactor(1, 0), new ConversionFactor(1, 0), 1, 0, new DimensionVector() { Length = 1 });
        public static UserUnit Foot= new UserUnit("FT", new ConversionFactor(1, 0), new ConversionFactor(1, 0), 0.3048m, 0, new DimensionVector() { Length = 1 });
        public static UserUnit Mile = new UserUnit("MI", new ConversionFactor(1, 0), new ConversionFactor(1, 0), 1609.344m, 0, new DimensionVector() { Length = 1 });

        //Time
        public static UserUnit Second = new UserUnit("S", new ConversionFactor(1, 0), new ConversionFactor(1, 0), 1, 0, new DimensionVector() { Time = 1 });
        public static UserUnit Hour = new UserUnit("HR", new ConversionFactor(1, 0), new ConversionFactor(1, 0), 3600, 0, new DimensionVector() { Time = 1 });

        //Mass
        public static UserUnit Kilogram = new UserUnit("KG", new ConversionFactor(1, 0), new ConversionFactor(1, 0), 1, 0, new DimensionVector() { Mass = 1 });
        public static UserUnit Pound = new UserUnit("LB", new ConversionFactor(1, 0), new ConversionFactor(1, 0), 0.45359237m, 0, new DimensionVector() { Mass = 1 });

        //Temperature
        public static UserUnit Kelvin = new UserUnit("K", new ConversionFactor(1, 0), new ConversionFactor(1, 0), 1, 0, new DimensionVector() { Temperature = 1 });
        public static UserUnit Celsius = new UserUnit("DEG_C", new ConversionFactor(1, 0), new ConversionFactor(1, 0), 1, 273.15m, new DimensionVector() { Temperature = 1 });
        public static UserUnit Fahrenheit = new UserUnit("DEG_F", new ConversionFactor(1, 0), new ConversionFactor(1, 0), 0.5555555555555556m, 459.669607m, new DimensionVector() { Temperature = 1 });

        public static UserUnit BritishThermalUnit = new UserUnit("BTU_IT", ConversionFactor.One, ConversionFactor.One,
            1055.05585262m, 0.0m, new DimensionVector() {Length = 2, Mass = 1, Time = -2});
    }
}
