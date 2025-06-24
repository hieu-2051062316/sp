using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace HanoConnect.API.Models
{
    [Table("Users")]
    public class User
    {
        [Key]
        [DatabaseGenerated(DatabaseGeneratedOption.Identity)]
        public int UserId { get; set; }

        [Required]
        [EmailAddress]
        [MaxLength(255)]
        public required string Email { get; set; }

        [Required]
        [MaxLength(255)]
        public required string PasswordHash { get; set; }

        [MaxLength(255)]
        public string? FullName { get; set; }

        [MaxLength(20)]
        public string? PhoneNumber { get; set; }

        [Column(TypeName = "Date")]
        public DateTime? DateOfBirth { get; set; }

        [MaxLength(100)]
        public string? District { get; set; }

        public DateTime CreatedAt { get; set; }
        public DateTime UpdatedAt { get; set; }

        // Navigation properties cho các mối quan hệ
        public ICollection<UserRole> UserRoles { get; set; } = new List<UserRole>();
        public Organization? Organization { get; set; }
        public ICollection<VolunteerSkill> VolunteerSkills { get; set; } = new List<VolunteerSkill>();
        public ICollection<VolunteerCause> VolunteerCauses { get; set; } = new List<VolunteerCause>();
        public ICollection<Application> Applications { get; set; } = new List<Application>();

        // Navigation property cho Notification
        public ICollection<Notification> Notifications { get; set; } = new List<Notification>();

        // Navigation properties cho Feedback
        public ICollection<Feedback> GivenFeedbacks { get; set; } = new List<Feedback>();
        public ICollection<Feedback> ReceivedFeedbacksAsRatedUser { get; set; } = new List<Feedback>();

        // Navigation property cho việc Admin xác thực tổ chức
        public ICollection<Organization> VerifiedOrganizations { get; set; } = new List<Organization>();
    }
}
