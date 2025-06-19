using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using static System.Net.Mime.MediaTypeNames;

namespace HanoConnect.API.Models
{
    [Table("Opportunities")]
    public class Opportunity
    {
        [Key]
        [DatabaseGenerated(DatabaseGeneratedOption.Identity)]
        public int OpportunityId { get; set; }

        public int OrganizationId { get; set; }

        [Required]
        [MaxLength(255)]
        public string Title { get; set; }

        [Required]
        [Column(TypeName = "NVARCHAR(MAX)")] // NVARCHAR(MAX)
        public string Description { get; set; }

        public int CauseId { get; set; }

        [MaxLength(255)]
        public string Location { get; set; }

        public DateTime? StartDate { get; set; }
        public DateTime? EndDate { get; set; }

        public bool IsFlexibleTime { get; set; } = false; // BIT DEFAULT 0

        public int? RequiredVolunteers { get; set; }

        [Column(TypeName = "NVARCHAR(MAX)")] // NVARCHAR(MAX)
        public string Benefits { get; set; }

        [MaxLength(255)]
        public string ContactInfo { get; set; }

        [Column(TypeName = "Date")] // DATE type
        public DateTime? ApplicationDeadline { get; set; }

        [MaxLength(50)]
        public string Status { get; set; } = "Open"; // DEFAULT 'Open'

        public bool IsApprovedByAdmin { get; set; } = false; // BIT DEFAULT 0

        public DateTime CreatedAt { get; set; }
        public DateTime UpdatedAt { get; set; }

        // Navigation properties
        [ForeignKey("OrganizationId")]
        public Organization Organization { get; set; }

        [ForeignKey("CauseId")]
        public Cause Cause { get; set; }

        public ICollection<OpportunitySkill> OpportunitySkills { get; set; }
        public ICollection<Application> Applications { get; set; }
    }
}