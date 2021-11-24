using System.Collections.Generic;
using System.Threading.Tasks;
using Bsc_In_Stream_Conversion.Model;

namespace Bsc_In_Stream_Conversion.Database
{
    public interface IDatabaseAccess
    {
        Task<bool> InsertUnitAsync(Unit unit);
        Task<int> InsertUnitsAsync(IEnumerable<Unit> units);
        Task<Unit> SelectUnit(string SystemName);
        Task<List<Unit>> SelectUnitByUnitName(string UnitName);
    }
}