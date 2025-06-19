using HanoConnect.API.Data;
using HanoConnect.API.Interfaces;
using HanoConnect.API.Models; // Ensure this is correctly referenced
using Microsoft.EntityFrameworkCore;

namespace HanoConnect.API.Repositories
{
    public class UserRepository : GenericRepository<User>, IUserRepository
    {
        public UserRepository(ApplicationDbContext context) : base(context)
        {
        }

        public async Task<User?> GetUserByEmailAsync(string email)
        {
            return await _dbSet.FirstOrDefaultAsync(u => u.Email == email);
        }
    }
}