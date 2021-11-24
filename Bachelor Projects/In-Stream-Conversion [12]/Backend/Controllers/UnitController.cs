using Bsc_In_Stream_Conversion.Database;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using Serilog;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace Bsc_In_Stream_Conversion.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class UnitController : ControllerBase
    {
        private readonly UnitFactory unitFactory;
        private readonly IDatabaseAccess db;

        public UnitController(UnitFactory unitFactory, IDatabaseAccess db)
        {
            this.unitFactory = unitFactory;
            this.db = db;
        }

        [HttpGet("{FromSystemName}/{ToSystemName}/{Value}")]
        public async Task<IActionResult> ConvertTo(string FromSystemName, string ToSystemName, decimal Value)
        {
            if(String.IsNullOrWhiteSpace(FromSystemName) || String.IsNullOrWhiteSpace(ToSystemName))
            {
                return NotFound();
            }

            try
            {
                FromSystemName = FromSystemName.Replace("47", "/");
                ToSystemName = ToSystemName.Replace("47", "/");

                var FromUnit = await unitFactory.Parse(FromSystemName);
                var ToUnit = await unitFactory.Parse(ToSystemName);

                if (FromUnit.DimensionVector != ToUnit.DimensionVector) throw new InvalidOperationException("Units do not have the same dimension vector");

                var convertedValue = ToUnit.ConvertFromBaseValue(FromUnit.ConvertToBaseValue(Value));

                return Ok(convertedValue);
            }catch(InvalidOperationException InvEx)
            {
                return BadRequest();
            }catch(Exception e)
            {
                Log.Error(e.Message + "\n" + e.StackTrace);
                return Problem();
            }
        }

        [HttpGet("{UnitName}")]
        public async Task<IActionResult> GetInfo(string UnitName)
        {
            var unit = await db.SelectUnitByUnitName(UnitName);
            return Ok(unit);
        }
    }
}
