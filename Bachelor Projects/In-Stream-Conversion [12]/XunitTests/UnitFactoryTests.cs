using System;
using System.Diagnostics;
using System.Threading.Tasks;
using Bsc_In_Stream_Conversion;
using Bsc_In_Stream_Conversion.Database;
using FluentAssertions;
using Xunit;
using XunitTests.Mocks;

namespace XunitTests
{
    public class UnitFactoryTests
    {
        [Theory]
        [InlineData("M", 1, 0)]
        [InlineData("S", 1, 0)]
        [InlineData("FT", 0.3048, 0)]
        [InlineData("KILOFT", 0.3048, 0)]
        [InlineData("PICOFT", 0.3048, 0)]
        [InlineData("FT^2", 0.09290304, 0)]
        [InlineData("DECIFT^2", 0.09290304, 0)]
        [InlineData("FT^3", 0.02831684659, 0)]
        [InlineData("MEGAFT^3", 0.02831684659, 0)]
        [InlineData("DEG_C", 1, 273.15)]
        [InlineData("M/S", 1, 0)]
        [InlineData("MI/HR", 0.44704, 0)]
        [InlineData("MI/KILOHR", 0.44704, 0)]
        [InlineData("KG*M", 1, 0)]
        [InlineData("LB*FT", 0.138254954376, 0)]
        [InlineData("M*DEG_F", 0.5555555556, 0)]
        [InlineData("DEG_C/HR", 0.0002777777777777777777777778, 0.0)]
        [InlineData("M/DEG_C", 1.0, 0.0)]
        [InlineData("BTU_IT/DEG_F*LB", 4186.8, 0.0)]
        [InlineData("KILOMI^3/HR^2", 321.6189680123904, 0.0)]
        public async Task CanParseInput(string input, decimal multiplier, decimal offset)
        {
            IDatabaseAccess db = new MockDatabase();
            var unitFactory = new UnitFactory(db);
            PerformanceMeasurer.StartTime = input.Replace("/", "47");
            Stopwatch w = new Stopwatch();
            for (int i = 0; i < 10000; i++)
            {
                w.Restart();
                var unit = await unitFactory.Parse(input);
                w.Stop();
                PerformanceMeasurer.Log(w.ElapsedTicks, input);
            }
            var unit1 = await unitFactory.Parse(input);
            await PerformanceMeasurer.DumpLog();
            Math.Abs(unit1.Multiplier - multiplier).Should().BeLessThan(0.0000000001m);
            Math.Abs(unit1.Offset - offset).Should().BeLessThan(0.0000000001m);
        }
    }
}
