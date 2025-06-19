using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
// using static System.Net.Mime.MediaTypeNames; // Dòng này có vẻ không cần thiết, bạn có thể xem xét xóa nếu không dùng

namespace HanoConnect.API.Models
{
    [Table("Users")] // Explicitly map to "Users" table
    public class User
    {
        [Key]
        [DatabaseGenerated(DatabaseGeneratedOption.Identity)]
        public int UserId { get; set; }

        [Required]
        [EmailAddress] // Provides client-side validation hint (not enforced by DB)
        [MaxLength(255)]
        public string Email { get; set; } // Không cần sửa, đã có [Required]

        [Required]
        [MaxLength(255)]
        public string PasswordHash { get; set; } // Không cần sửa, đã có [Required]

        [MaxLength(255)]
        public string? FullName { get; set; } // <--- Thêm '?'

        [MaxLength(20)]
        public string? PhoneNumber { get; set; } // <--- Thêm '?'

        [Column(TypeName = "Date")] // Maps to SQL Server DATE type
        public DateTime? DateOfBirth { get; set; } // Đã là nullable, không cần sửa

        [MaxLength(100)]
        public string? District { get; set; } // <--- Thêm '?'

        public DateTime CreatedAt { get; set; } // DateTime là non-nullable, thường được gán giá trị khi tạo đối tượng. Không cần sửa nếu bạn gán nó khi khởi tạo.
        public DateTime UpdatedAt { get; set; } // Tương tự CreatedAt

        // Navigation properties for relationships
        public ICollection<UserRole> UserRoles { get; set; } = new List<UserRole>(); // <--- Khởi tạo
        public Organization? Organization { get; set; } // <--- Thêm '?' (vì một user có thể không phải là tài khoản tổ chức)
        public ICollection<VolunteerSkill> VolunteerSkills { get; set; } = new List<VolunteerSkill>(); // <--- Khởi tạo
        public ICollection<VolunteerCause> VolunteerCauses { get; set; } = new List<VolunteerCause>(); // <--- Khởi tạo
        public ICollection<Application> Applications { get; set; } = new List<Application>(); // <--- Khởi tạo

        // Feedback relationships:
        public ICollection<Feedback> GivenFeedbacks { get; set; } = new List<Feedback>(); // <--- Khởi tạo
        public ICollection<Feedback> ReceivedFeedbacksAsRatedUser { get; set; } = new List<Feedback>(); // <--- Khởi tạo

        // For Admin verifying organizations
        public ICollection<Organization> VerifiedOrganizations { get; set; } = new List<Organization>(); // <--- Khởi tạo
    }
}