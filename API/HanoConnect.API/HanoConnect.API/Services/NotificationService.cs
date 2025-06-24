using HanoConnect.API.DTOs;
using HanoConnect.API.Interfaces;
using HanoConnect.API.Models;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace HanoConnect.API.Services
{
    public class NotificationService : INotificationService
    {
        private readonly INotificationRepository _notificationRepository;

        public NotificationService(INotificationRepository notificationRepository)
        {
            _notificationRepository = notificationRepository;
        }

        public async Task CreateNotificationAsync(int userId, string message)
        {
            var notification = new Notification
            {
                UserId = userId,
                Message = message,
            };
            await _notificationRepository.AddAsync(notification);
            await _notificationRepository.SaveChangesAsync();
        }

        public async Task<IEnumerable<NotificationDto>> GetNotificationsForUserAsync(int userId)
        {
            var notifications = await _notificationRepository.GetByUserIdAsync(userId);
            return notifications.Select(n => new NotificationDto
            {
                Id = n.Id,
                Message = n.Message,
                IsRead = n.IsRead,
                CreatedAt = n.CreatedAt
            });
        }
    }
}
