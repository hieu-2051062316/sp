using HanoConnect.API.DTOs;
using HanoConnect.API.Interfaces;
using Microsoft.AspNetCore.Mvc;
using System.Collections.Generic;
using System.Threading.Tasks;

namespace HanoConnect.API.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class NotificationsController : ControllerBase
    {
        private readonly INotificationService _notificationService;

        public NotificationsController(INotificationService notificationService)
        {
            _notificationService = notificationService;
        }

        // Lấy tất cả thông báo cho một người dùng
        [HttpGet("user/{userId}")]
        public async Task<ActionResult<IEnumerable<NotificationDto>>> GetForUser(int userId)
        {
            var notifications = await _notificationService.GetNotificationsForUserAsync(userId);
            return Ok(notifications);
        }
    }
}
