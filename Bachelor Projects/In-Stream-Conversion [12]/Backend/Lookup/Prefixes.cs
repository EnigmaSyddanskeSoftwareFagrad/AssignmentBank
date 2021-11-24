using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace Bsc_In_Stream_Conversion.Lookup
{
    public class Prefixes
    {
        public class ConversionFactor
        {
            public static ConversionFactor One = new ConversionFactor(10, 0);

            public int Base;
            public double Factor;

            public ConversionFactor(int @base, double factor)
            {
                Base = @base;
                Factor = factor;
            }

            public static ConversionFactor operator *(ConversionFactor cf1, ConversionFactor cf2)
            {
                if (cf1.Base != cf2.Base)
                {
                    throw new InvalidOperationException("Cannot add conversion factors of different bases");
                }
                return new ConversionFactor(cf1.Base, cf1.Factor + cf2.Factor);
            }

            public static bool operator ==(ConversionFactor cf1, ConversionFactor cf2)
            {
                return cf1.Base == cf2.Base && cf1.Factor == cf2.Factor;
            }

            public static bool operator !=(ConversionFactor cf1, ConversionFactor cf2)
            {
                return cf1.Base != cf2.Base || cf1.Factor != cf2.Factor;
            }
            
            public override bool Equals(Object cf1)
            {
                if (cf1 is ConversionFactor)
                {
                    var x = (ConversionFactor)cf1;
                    return x.Base == Base && x.Factor == Factor;
                }
                else
                {
                    return false;
                }
            }

            public override int GetHashCode()
            {
                return HashCode.Combine(Base, Factor);
            }
        }

        public static Dictionary<string, ConversionFactor> IntegerFactor = new Dictionary<string, ConversionFactor>()
        {
            {"YOTTA", new ConversionFactor(10, 24)},
            {"ZETTA", new ConversionFactor(10, 21)},
            {"EXA", new ConversionFactor(10, 18) },
            {"PETA", new ConversionFactor(10, 15) },
            {"TERA", new ConversionFactor(10, 12) },
            {"GIGA", new ConversionFactor(10, 9) },
            {"MEGA", new ConversionFactor(10, 6) },
            {"KILO", new ConversionFactor(10, 3) },
            {"HECTO", new ConversionFactor(10, 2) },
            {"DECA", new ConversionFactor(10, 1) },
            {"", new ConversionFactor(10,1) },
            {"DECI", new ConversionFactor(10, -1) },
            {"CENTI", new ConversionFactor(10, -2) },
            {"MILLI", new ConversionFactor(10, -3) },
            {"MICRO", new ConversionFactor(10, -6) },
            {"NANO", new ConversionFactor(10, -9) },
            {"PICO", new ConversionFactor(10, -12) },
            {"FEMTO", new ConversionFactor(10, -15) },
            {"ATTO", new ConversionFactor(10, -18) },
            {"ZEPTO", new ConversionFactor(10, -21) },
            {"YOCTO", new ConversionFactor(10, -24) },
            {"KIBI", new ConversionFactor(10, 3.01029995664) },
            {"MIBI", new ConversionFactor(10, 6.02059991328) },
            {"GIBI", new ConversionFactor(10, 9.03089986992) },
            {"TEBI", new ConversionFactor(10, 12.0411998266) },
            {"PEBI", new ConversionFactor(10, 15.0514997832) },
            {"EXBI", new ConversionFactor(10, 18.0617997398) },
            {"ZEBI", new ConversionFactor(10, 21.0720996965) },
            {"YOBI", new ConversionFactor(10, 24.0823996531) }
        };
    }
}
