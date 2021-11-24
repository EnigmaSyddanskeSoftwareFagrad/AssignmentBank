using System;
using System.Collections.Generic;
using System.Net.Http;
using System.Threading;
using System.Threading.Tasks;
using Common;
using Microsoft.AspNetCore.SignalR.Client;
using Newtonsoft.Json;

namespace TestClient
{
    class Program
    {
        public static int trackTime = 30000;
        public static int threadCount = 50;
        public static string helpMessage = "Usage:\n" +
        "\t-h, --help, h, help: returns this message\n" +
        "\tFirst argument: \"auto\" if the program should start tracking, stop tracking and dump data automatically\n" +
        "\tSecond Argument: the number that should be inserted in the dump count for the auto version";
        public static string runningHelpMessage = "\tstart: Starts measuring incoming data\n" +
                                                        "\tstop: Stops measuring incoming data\n" +
                                                        "\tdump: Writes all measurements to a file\n" +
                                                        "\texit: Exits the program\n" +
                                                        "\thelp: Display this message";
        private static List<HubConnection> conns = new List<HubConnection>();
        private static bool currentlyMeasuring = false;
        static async Task Main(string[] args)
        {
            if(args.Length < 1)
            {
                Console.WriteLine(helpMessage);
                return;
            }
            args[0] = args[0].ToLower();
            if (args[0] == "-h" || args[0] == "--help" || args[0] == "h" || args[0] == "help")
            {
                Console.WriteLine(helpMessage);
                return;
            }
            Console.Out.Flush();
            PerformanceMeasurer.dumpnr = int.Parse(args[1]);
            PerformanceMeasurer.StartTime = DateTimeOffset.UtcNow.ToUnixTimeSeconds().ToString();
            Console.WriteLine($"Starting {PerformanceMeasurer.StartTime}...");

            while(conns.Count < threadCount) { 
                await CreateClient();
            }

            await Task.Delay(1000); //Delay the start of the logging to prevent outliers
            if(args[0] == "auto" && args.Length >= 2)
            {
                Console.WriteLine(PerformanceMeasurer.dumpnr);
                Console.WriteLine("Starting Measurement");
                currentlyMeasuring = true;
                Thread.Sleep(trackTime);
                currentlyMeasuring = false;
                Console.WriteLine("Dumping Measurement");
                PerformanceMeasurer.DumpLog();
                Console.ReadLine();
            }
            else {
                Console.WriteLine(runningHelpMessage);
                string input = "";
                while(input != "exit")
                {
                    input = Console.ReadLine().ToLowerInvariant();
                    switch (input)
                    {
                        case "start": currentlyMeasuring = true;break;
                        case "stop": currentlyMeasuring = false;break;
                        case "dump": PerformanceMeasurer.DumpLog(); break;
                        default: Console.WriteLine(runningHelpMessage);break;
                    }
                }
            }

        }

        private static async Task CreateClient()
        {
            Console.WriteLine($"Live clients: {conns.Count + 1}");
            await Task.Run(async () =>
            {
                HubConnection conn = new HubConnectionBuilder()
               .WithAutomaticReconnect()
               .WithUrl("REMOVE FOR REASON", (opts) =>
               {
                   opts.HttpMessageHandlerFactory = (message) =>
                   {
                       if (message is HttpClientHandler clientHandler)
                           // bypass SSL certificate
                           clientHandler.ServerCertificateCustomValidationCallback +=
                           (sender, certificate, chain, sslPolicyErrors) => { return true; };
                       return message;
                   };
               })
               .Build();

                conns.Add(conn);

                Guid clientId = Guid.NewGuid();
                
                conn.On<string>("NewData", data =>
                {
                    try
                    {
                        if (currentlyMeasuring)
                        {
                            PerformanceMeasurer.Log(JsonConvert.DeserializeObject<ClientMessageDto>(data), DateTimeOffset.UtcNow.ToUnixTimeMilliseconds(), clientId);

                        }
                    }
                    catch (Exception e)
                    {
                        Console.Error.WriteLine(e.Message + e.StackTrace);
                    }
                });

                try
                {
                    await conn.StartAsync();
                    //Console.WriteLine("Connection started");
                }
                catch (Exception e)
                {
                    Console.WriteLine(e.Message);
                    Console.WriteLine(e.StackTrace);
                    conns.Remove(conn);
                }

                try
                {
                    await conn.InvokeAsync("SubscribeTo", "REMOVED", "K");
                }
                catch (Exception e)
                {
                    Console.WriteLine(e.Message);
                    Console.WriteLine(e.StackTrace);
                    conns.Remove(conn);
                }

                if (conn.State == HubConnectionState.Connected)
                {
                    return;
                }
                else
                {
                    conns.Remove(conn);
                }
            });
        }
    }
}
