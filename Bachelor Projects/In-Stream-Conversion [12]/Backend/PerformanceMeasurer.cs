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

namespace Bsc_In_Stream_Conversion
{
    public class PerformanceMeasurer
    {
        private static int NumberOfLogFiles = 0;
        public static string StartTime = "";
        private static ConcurrentQueue<Entry> log = new ConcurrentQueue<Entry>();
        private static bool IsRunning = false;
        private static Semaphore _lock = new Semaphore(1,1);

        public static void Log(int NumberOfThreads, long Ticks, int ThreadId)
        {
            _lock.WaitOne();
            var e = new Entry()
            {
                NumberOfThreads = NumberOfThreads,
                Ticks = Ticks,
                ThreadId = ThreadId
            };
            log.Enqueue(e);
            _lock.Release();
        }

        public static async Task DumpLog()
        {
            try
            {
                if (IsRunning) return;
                IsRunning = true;
                await Task.Delay(60000);
                //using (var fs = File.Create(Environment.GetFolderPath(Environment.SpecialFolder.MyDocuments) + $"/UnitPerformance/{StartTime}Dump{++NumberOfLogFiles}.txt"))
                using (var fs = new StreamWriter(new FileStream(Environment.GetFolderPath(Environment.SpecialFolder.MyDocuments) + $"/UnitPerformance/{StartTime}Dump{++NumberOfLogFiles:000}.txt", FileMode.OpenOrCreate)))
                {
                    _lock.WaitOne();
                    var array = log.ToArray();
                    log = new ConcurrentQueue<Entry>();
                    _lock.Release();
                    fs.Write("Tickrate: " + Stopwatch.Frequency+"\n");
                    foreach (var entry in array)
                    {
                        fs.Write($"NumberOfThreads:{entry.NumberOfThreads}:Ticks:{entry.Ticks}:ThreadId:{entry.ThreadId}\n");
                    }
                    fs.Flush();
                    fs.Close();
                }
                IsRunning = false;
            }
            catch (Exception e)
            {
                _lock.Release();
                Serilog.Log.Error(e.Message + ":" + e.StackTrace);
            }
        }

        private class Entry
        {
            public int NumberOfThreads { get; set; }
            public long Ticks { get; set; }
            public int ThreadId { get; set; }
        }
    }
}
