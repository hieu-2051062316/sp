using HanoConnect.API.DTOs;
using System.Collections.Generic;
using System.Threading.Tasks;

namespace HanoConnect.API.Interfaces
{
    public interface INotificationService
    {
        Task<IEnumerable<NotificationDto>> GetNotificationsForUserAsync(int userId);
        Task CreateNotificationAsync(int userId, string message);
    }
}
