using HanoConnect.API.Data;
using HanoConnect.API.Interfaces;
using HanoConnect.API.Models;
using Microsoft.EntityFrameworkCore;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace HanoConnect.API.Repositories
{
    public class NotificationRepository : GenericRepository<Notification>, INotificationRepository
    {
        public NotificationRepository(ApplicationDbContext context) : base(context)
        {
        }

        public async Task<IEnumerable<Notification>> GetByUserIdAsync(int userId)
        {
            return await _dbSet
                .Where(n => n.UserId == userId)
                .OrderByDescending(n => n.CreatedAt)
                .ToListAsync();
        }
    }
}
