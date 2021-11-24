using Common;
using Newtonsoft.Json;
using Serilog;
using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Globalization;
using System.Linq;
using System.Net.WebSockets;
using System.Threading;
using System.Threading.Tasks;
using Bsc_In_Stream_Conversion.MQTT;

namespace Bsc_In_Stream_Conversion
{
    public class SocketRequestHandler
    {
        private List<string> messages = new List<string>();
        private Guid subscribtionId;
        private UserUnit FromUnit;

        private string topic;
        private UserUnit toUnit;
        private IStreamClientManager mqttClientManager;
        private Func<string, object[], CancellationToken, Task> answerCallback;
        private readonly UnitFactory unitFactory;

        public SocketRequestHandler(IStreamClientManager mqttClientManager, UnitFactory unitFactory)
        {
            this.mqttClientManager = mqttClientManager;
            this.unitFactory = unitFactory;
        }

        public async Task Subscribe(string topic, string toUnit, Func<string, object[], CancellationToken, Task> answerCallback)
        {
            this.topic = topic;
            this.toUnit = await unitFactory.Parse(toUnit);
            this.answerCallback = answerCallback;
            FromUnit = await unitFactory.Parse(topic.Split("/").Last().Replace("%F2", "/"));
            if (this.toUnit.DimensionVector != FromUnit.DimensionVector) throw new InvalidOperationException("Units do not have the same dimension vector");
            subscribtionId = await mqttClientManager.Subscribe(topic, HandleNewMessage);
        }

        internal void Unsubscribe()
        {

            try
            {
                mqttClientManager.Unsubscribe(subscribtionId);
            }
            catch(Exception e)
            {
                Log.Error(e.Message + ":" + e.StackTrace);
            }
        }

        private async Task HandleNewMessage(string message)
        {
            try
            {
#if PERFORMANCE || false
                Stopwatch timer = new Stopwatch();
                timer.Start();
#endif
                var msgObject = JsonConvert.DeserializeObject<ReadingDto>(message);
                var value = msgObject.Reading;
                
                var convertedValue = toUnit.ConvertFromBaseValue(FromUnit.ConvertToBaseValue(value));

                
                var clientMessage = new ClientMessageDto(msgObject, Thread.CurrentThread.ManagedThreadId, mqttClientManager.GetCurrentThreadCount(), convertedValue);
#if PERFORMANCE || false
                timer.Stop();
                PerformanceMeasurer.Log(mqttClientManager.GetCurrentThreadCount(), timer.ElapsedTicks, Thread.CurrentThread.ManagedThreadId);
                PerformanceMeasurer.DumpLog();
#endif
                await answerCallback("NewData", new object[] { JsonConvert.SerializeObject(clientMessage) }, CancellationToken.None);

            }
            catch (Exception e)
            {
                Log.Error("Error sending message " + e.StackTrace, e);
            }
        }
    }
}
