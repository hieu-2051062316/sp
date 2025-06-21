using System;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace HanoConnect.API.Models
{
    [Table("Feedback")]
    public class Feedback
    {
        [Key]
        [DatabaseGenerated(DatabaseGeneratedOption.Identity)]
        public int FeedbackId { get; set; }

        public int? ApplicationId { get; set; } // Nullable FK to Applications
        public int RaterUserId { get; set; } // Not Null
        public int? RatedUserId { get; set; } // Nullable FK to Users
        public int? RatedOrganizationId { get; set; } // Nullable FK to Organizations

        public int? Score { get; set; } // Nullable, as in your schema

        [Column(TypeName = "NVARCHAR(MAX)")] // NVARCHAR(MAX)
        public string? Comment { get; set; } // <--- Đã thêm '?' để cho phép null, giải quyết cảnh báo CS8618

        public DateTime FeedbackTime { get; set; } // DEFAULT GETDATE()

        // Navigation properties
        [ForeignKey("ApplicationId")]
        public Application? Application { get; set; } // <--- Đã thêm '?' để khớp với ApplicationId (nullable)

        [ForeignKey("RaterUserId")]
        public required User RaterUser { get; set; } // <--- Đã thêm 'required' để khớp với RaterUserId (not null)

        [ForeignKey("RatedUserId")]
        public User? RatedUser { get; set; } // <--- Đã thêm '?' để khớp với RatedUserId (nullable)

        [ForeignKey("RatedOrganizationId")]
        public Organization? RatedOrganization { get; set; } // <--- Đã thêm '?' để khớp với RatedOrganizationId (nullable)
    }
}