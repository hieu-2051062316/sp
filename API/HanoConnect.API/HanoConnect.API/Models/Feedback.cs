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
        public string Comment { get; set; }

        public DateTime FeedbackTime { get; set; } // DEFAULT GETDATE()

        // Navigation properties
        [ForeignKey("ApplicationId")]
        public Application Application { get; set; }

        [ForeignKey("RaterUserId")]
        public User RaterUser { get; set; } // User who gave the feedback

        [ForeignKey("RatedUserId")]
        public User RatedUser { get; set; } // User who received the feedback (if applicable)

        [ForeignKey("RatedOrganizationId")]
        public Organization RatedOrganization { get; set; } // Organization that received the feedback (if applicable)
    }
}