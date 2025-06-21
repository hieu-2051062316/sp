using HanoConnect.API.Data;
using HanoConnect.API.Interfaces;
using HanoConnect.API.Models;
using Microsoft.EntityFrameworkCore; // Để sử dụng FirstOrDefaultAsync

namespace HanoConnect.API.Repositories
{
    public class SkillRepository : GenericRepository<Skill>, ISkillRepository
    {
        public SkillRepository(ApplicationDbContext context) : base(context)
        {
        }

        public async Task<Skill?> GetSkillByNameAsync(string skillName)
        {
            return await _dbSet.FirstOrDefaultAsync(s => s.SkillName == skillName);
        }
    }
}