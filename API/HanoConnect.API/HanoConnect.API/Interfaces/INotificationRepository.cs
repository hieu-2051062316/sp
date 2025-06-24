using HanoConnect.API.Models;
using System.Collections.Generic;
using System.Threading.Tasks;

namespace HanoConnect.API.Interfaces
{
    public interface INotificationRepository : IGenericRepository<Notification>
    {
        Task<IEnumerable<Notification>> GetByUserIdAsync(int userId);
    }
}
