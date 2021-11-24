using System;
using System.Threading.Tasks;

namespace Bsc_In_Stream_Conversion.MQTT
{
    public interface IStreamClientManager
    {
        Task<Guid> Subscribe(string topic, Func<string, Task> messageCallback);

        Task Unsubscribe(Guid subId);

        Task PublishMessageAsync(string topic, string message);

        int GetCurrentThreadCount();
    }
}
