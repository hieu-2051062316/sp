using HanoConnect.API.Models;
using System.Collections.Generic;
using System.Threading.Tasks;

namespace HanoConnect.API.Interfaces
{
    public interface ICauseService
    {
        Task<IEnumerable<Cause>> GetAllCausesAsync();
        Task<Cause?> GetCauseByIdAsync(int id);
        Task<Cause?> AddCauseAsync(Cause cause);
        Task<bool> UpdateCauseAsync(Cause cause);
        Task<bool> DeleteCauseAsync(int id);
        Task<Cause?> GetCauseByNameAsync(string causeName);
    }
}