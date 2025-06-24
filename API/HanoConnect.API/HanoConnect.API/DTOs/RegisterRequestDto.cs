using System.ComponentModel.DataAnnotations;

namespace HanoConnect.API.DTOs
{
    // DTO để xử lý dữ liệu đăng ký
    public class RegisterRequestDto
    {
        [Required]
        [EmailAddress]
        public string Email { get; set; }

        [Required]
        [MinLength(6)]
        public string Password { get; set; }

        [Required]
        public string Role { get; set; } // "Volunteer" hoặc "Organization"

        // Trường này chỉ bắt buộc cho Volunteer
        public string? FullName { get; set; }

        // Trường này chỉ bắt buộc cho Organization
        public string? OrganizationName { get; set; }
    }
}
