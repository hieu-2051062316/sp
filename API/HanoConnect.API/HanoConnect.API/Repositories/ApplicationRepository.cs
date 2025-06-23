using HanoConnect.API.Data;
using HanoConnect.API.Interfaces;
using HanoConnect.API.Models;
using Microsoft.EntityFrameworkCore;
using System.Threading.Tasks;

namespace HanoConnect.API.Repositories
{
    public class ApplicationRepository : GenericRepository<Application>, IApplicationRepository
    {
        public ApplicationRepository(ApplicationDbContext context) : base(context)
        {
        }

        public async Task<Application?> FindByUserAndOpportunityAsync(int volunteerUserId, int opportunityId)
        {
            return await _dbSet.FirstOrDefaultAsync(a => a.VolunteerUserId == volunteerUserId && a.OpportunityId == opportunityId);
        }
    }
}
