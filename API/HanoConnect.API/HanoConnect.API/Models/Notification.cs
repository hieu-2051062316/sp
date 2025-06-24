using System;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace HanoConnect.API.Models
{
    [Table("Notifications")]
    public class Notification
    {
        [Key]
        public int Id { get; set; }

        [Required]
        public int UserId { get; set; } // ID của người nhận thông báo

        [Required]
        public string Message { get; set; }

        public bool IsRead { get; set; } = false;

        public DateTime CreatedAt { get; set; } = DateTime.UtcNow;

        // Có thể thêm các trường khác như Link, NotificationType,...
        // public string? ActionLink { get; set; }

        [ForeignKey("UserId")]
        public User User { get; set; }
    }
}
