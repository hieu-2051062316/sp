using System;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace HanoConnect.API.Models
{
    [Table("Applications")]
    public class Application
    {
        [Key]
        [DatabaseGenerated(DatabaseGeneratedOption.Identity)]
        public int ApplicationId { get; set; }

        public int OpportunityId { get; set; }
        public int VolunteerUserId { get; set; }

        public DateTime ApplicationTime { get; set; } // DEFAULT GETDATE()

        [Column(TypeName = "NVARCHAR(MAX)")] // NVARCHAR(MAX)
        public string MotivationLetter { get; set; }

        [MaxLength(50)]
        public string Status { get; set; } = "Pending"; // DEFAULT 'Pending'

        [Column(TypeName = "NVARCHAR(MAX)")] // NVARCHAR(MAX)
        public string OrganizationNotes { get; set; }

        // Navigation properties
        [ForeignKey("OpportunityId")]
        public Opportunity Opportunity { get; set; }

        [ForeignKey("VolunteerUserId")]
        public User VolunteerUser { get; set; }

        // Feedback can be associated with an application (nullable FK in DB)
        // public Feedback Feedback { get; set; } // If one-to-one or one-to-many from Application to Feedback
    }
}