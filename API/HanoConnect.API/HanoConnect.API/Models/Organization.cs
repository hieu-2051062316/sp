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
        public required string OrganizationName { get; set; } // Đã thêm từ khóa 'required'

        [MaxLength(255)]
        public string? ContactPerson { get; set; }

        [MaxLength(20)]
        public string? ContactPhone { get; set; }

        [MaxLength(255)]
        public string? Address { get; set; }

        [MaxLength(255)]
        public string? Website { get; set; }

        [Column(TypeName = "NVARCHAR(MAX)")] // Maps to SQL Server NVARCHAR(MAX)
        public string? Description { get; set; }

        public bool IsVerified { get; set; }

        public int? VerifiedByAdminId { get; set; }
        public DateTime? VerificationTime { get; set; }

        public DateTime CreatedAt { get; set; }
        public DateTime UpdatedAt { get; set; }

        // Navigation properties
        [ForeignKey("UserId")]
        public required User User { get; set; }

        [ForeignKey("VerifiedByAdminId")]
        public User? VerifiedByAdmin { get; set; }

        public ICollection<Opportunity> Opportunities { get; set; } = new List<Opportunity>();
        public ICollection<Feedback> ReceivedFeedbacks { get; set; } = new List<Feedback>();
    }
}