using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using static System.Net.Mime.MediaTypeNames;

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
        public string Email { get; set; }

        [Required]
        [MaxLength(255)]
        public string PasswordHash { get; set; } // Stores hashed password

        [MaxLength(255)]
        public string FullName { get; set; }

        [MaxLength(20)]
        public string PhoneNumber { get; set; }

        [Column(TypeName = "Date")] // Maps to SQL Server DATE type
        public DateTime? DateOfBirth { get; set; } // Nullable DATE

        [MaxLength(100)]
        public string District { get; set; }

        public DateTime CreatedAt { get; set; } // DATETIME2 DEFAULT GETDATE() in DB
        public DateTime UpdatedAt { get; set; } // DATETIME2 DEFAULT GETDATE() in DB

        // Navigation properties for relationships
        public ICollection<UserRole> UserRoles { get; set; }
        public Organization Organization { get; set; } // One-to-one with Organization (if user is an organization's account)
        public ICollection<VolunteerSkill> VolunteerSkills { get; set; }
        public ICollection<VolunteerCause> VolunteerCauses { get; set; }
        public ICollection<Application> Applications { get; set; }

        // Feedback relationships:
        public ICollection<Feedback> GivenFeedbacks { get; set; } // Feedbacks given BY this user (as RaterUserId)
        public ICollection<Feedback> ReceivedFeedbacksAsRatedUser { get; set; } // Feedbacks received BY this user (as RatedUserId)

        // For Admin verifying organizations
        public ICollection<Organization> VerifiedOrganizations { get; set; }
    }
}