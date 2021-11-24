using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Common
{
    public class ReadingDto
    {
        public long TimeOfReading { get; set; }
        public decimal Reading { get; set; }
        public Guid ReadingId { get; set; }
        public long MessageCounterId { get; set; }
    }
}
