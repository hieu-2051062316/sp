using HanoConnect.API.Data;
using HanoConnect.API.Interfaces;
using HanoConnect.API.Models;
using Microsoft.EntityFrameworkCore;

namespace HanoConnect.API.Repositories
{
    public class OrganizationRepository : GenericRepository<Organization>, IOrganizationRepository
    {
        public OrganizationRepository(ApplicationDbContext context) : base(context)
        {
        }

        public async Task<Organization?> GetOrganizationByUserIdAsync(int userId)
        {
            return await _dbSet.FirstOrDefaultAsync(o => o.UserId == userId);
        }

        public async Task<Organization?> GetOrganizationByNameAsync(string organizationName)
        {
            return await _dbSet.FirstOrDefaultAsync(o => o.OrganizationName == organizationName);
        }
    }
}