using System;
using System.Collections.Concurrent;
using System.Collections.Generic;
using System.Diagnostics;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading;
using System.Threading.Tasks;
using Serilog;

namespace XunitTests
{
    public class PerformanceMeasurer
    {
        public static string StartTime = "Test1";
        private static ConcurrentQueue<Entry> log = new ConcurrentQueue<Entry>();

        public static void Log(long ms, string unit)
        {
            var e = new Entry()
            {
                ms = ms,
                unit = unit
            };
            log.Enqueue(e);
        }

        public static async Task DumpLog()
        {
            try
            {
                //using (var fs = File.Create(Environment.GetFolderPath(Environment.SpecialFolder.MyDocuments) + $"/UnitPerformance/{StartTime}Dump{++NumberOfLogFiles}.txt"))
                using (var fs = new StreamWriter(new FileStream(Environment.GetFolderPath(Environment.SpecialFolder.MyDocuments) + $"/UnitPerformance/{StartTime}ParsePerformance.txt", FileMode.OpenOrCreate)))
                {
                    var array = log.ToArray();
                    log = new ConcurrentQueue<Entry>();
                    foreach (var entry in array)
                    {
                        fs.Write($"ticks:{entry.ms}:unit:{entry.unit}\n");
                    }
                    fs.Flush();
                    fs.Close();
                }
            }
            catch (Exception e)
            {
                Serilog.Log.Error(e.Message + ":" + e.StackTrace);
            }
        }

        private class Entry
        {
            public long ms { get; set; }
            public string unit { get; set; }
        }
    }
}
