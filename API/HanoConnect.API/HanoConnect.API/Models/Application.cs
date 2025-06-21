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

        public DateTime ApplicationTime { get; set; }

        [Column(TypeName = "NVARCHAR(MAX)")]
        public string? MotivationLetter { get; set; }

        [MaxLength(50)]
        public string Status { get; set; } = "Pending";

        [Column(TypeName = "NVARCHAR(MAX)")]
        public string? OrganizationNotes { get; set; }

        // Navigation properties
        [ForeignKey("OpportunityId")]
        public required Opportunity Opportunity { get; set; }

        [ForeignKey("VolunteerUserId")]
        public required User VolunteerUser { get; set; }
    }
}
