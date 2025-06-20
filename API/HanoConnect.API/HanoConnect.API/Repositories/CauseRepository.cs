using HanoConnect.API.Data;
using HanoConnect.API.Interfaces;
using HanoConnect.API.Models;
using Microsoft.EntityFrameworkCore;

namespace HanoConnect.API.Repositories
{
    public class CauseRepository : GenericRepository<Cause>, ICauseRepository
    {
        public CauseRepository(ApplicationDbContext context) : base(context)
        {
        }

        public async Task<Cause?> GetCauseByNameAsync(string causeName)
        {
            return await _dbSet.FirstOrDefaultAsync(c => c.CauseName == causeName);
        }
    }
}