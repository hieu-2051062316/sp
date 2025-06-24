using System.ComponentModel.DataAnnotations;

namespace HanoConnect.API.DTOs
{
    // DTO này được mở rộng để lấy đầy đủ thông tin khi đăng ký
    public class RegisterRequestDto
    {
        [Required, EmailAddress]
        public string Email { get; set; }

        [Required, MinLength(6)]
        public string Password { get; set; }

        [Required]
        public string Role { get; set; } // "Volunteer" hoặc "Organization"

        [Required]
        public string FullName { get; set; } // Tên TNV hoặc Tên người liên hệ của Tổ chức

        // Các trường cho Volunteer
        public string? PhoneNumber { get; set; }
        public string? District { get; set; }

        // Các trường cho Organization
        public string? OrganizationName { get; set; }
        public string? Address { get; set; }
        public string? Website { get; set; }
        public string? Description { get; set; }
    }
}
