using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace HanoConnect.API.Models
{
    [Table("Organizations")]
    public class Organization
    {
        [Key]
        [DatabaseGenerated(DatabaseGeneratedOption.Identity)]
        public int OrganizationId { get; set; }

        public int UserId { get; set; } // FK to Users table (Unique in DB)

        [Required]
        [MaxLength(255)]
        public string OrganizationName { get; set; }

        [MaxLength(255)]
        public string ContactPerson { get; set; }

        [MaxLength(20)]
        public string ContactPhone { get; set; }

        [MaxLength(255)]
        public string Address { get; set; }

        [MaxLength(255)]
        public string Website { get; set; }

        [Column(TypeName = "NVARCHAR(MAX)")] // Maps to SQL Server NVARCHAR(MAX)
        public string Description { get; set; }

        public bool IsVerified { get; set; } // BIT DEFAULT 0

        public int? VerifiedByAdminId { get; set; } // Nullable FK to Admin User
        public DateTime? VerificationTime { get; set; } // Nullable DateTime

        public DateTime CreatedAt { get; set; }
        public DateTime UpdatedAt { get; set; }

        // Navigation properties
        [ForeignKey("UserId")]
        public User User { get; set; } // The user account associated with this organization

        [ForeignKey("VerifiedByAdminId")]
        public User VerifiedByAdmin { get; set; } // The admin user who verified this organization (nullable)

        public ICollection<Opportunity> Opportunities { get; set; } // Opportunities posted by this organization

        // Feedback relationships
        public ICollection<Feedback> ReceivedFeedbacks { get; set; } // Feedbacks received by this organization (as RatedOrganizationId)
    }
}