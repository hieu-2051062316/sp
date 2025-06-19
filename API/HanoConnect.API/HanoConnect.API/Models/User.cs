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
        public required string Email { get; set; } // Đã thêm từ khóa 'required'

        [Required]
        [MaxLength(255)]
        public required string PasswordHash { get; set; } // Đã thêm từ khóa 'required'

        [MaxLength(255)]
        public string? FullName { get; set; }

        [MaxLength(20)]
        public string? PhoneNumber { get; set; }

        [Column(TypeName = "Date")] // Maps to SQL Server DATE type
        public DateTime? DateOfBirth { get; set; }

        [MaxLength(100)]
        public string? District { get; set; }

        public DateTime CreatedAt { get; set; }
        public DateTime UpdatedAt { get; set; }

        // Navigation properties for relationships
        public ICollection<UserRole> UserRoles { get; set; } = new List<UserRole>();
        public Organization? Organization { get; set; }
        public ICollection<VolunteerSkill> VolunteerSkills { get; set; } = new List<VolunteerSkill>();
        public ICollection<VolunteerCause> VolunteerCauses { get; set; } = new List<VolunteerCause>();
        public ICollection<Application> Applications { get; set; } = new List<Application>();

        // Feedback relationships:
        public ICollection<Feedback> GivenFeedbacks { get; set; } = new List<Feedback>();
        public ICollection<Feedback> ReceivedFeedbacksAsRatedUser { get; set; } = new List<Feedback>();

        // For Admin verifying organizations
        public ICollection<Organization> VerifiedOrganizations { get; set; } = new List<Organization>();
    }
}