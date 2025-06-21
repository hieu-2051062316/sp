using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
// using static System.Net.Mime.MediaTypeNames; // Dòng này có vẻ không cần thiết, bạn có thể xem xét xóa nếu không dùng

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
        public required string Title { get; set; }

        [Column(TypeName = "NVARCHAR(MAX)")]
        public string? Description { get; set; }

        public int CauseId { get; set; }

        [MaxLength(255)]
        public string? Location { get; set; }

        public DateTime? StartDate { get; set; }
        public DateTime? EndDate { get; set; }

        public bool IsFlexibleTime { get; set; } = false;

        public int? RequiredVolunteers { get; set; }

        [Column(TypeName = "NVARCHAR(MAX)")]
        public string? Benefits { get; set; }

        [MaxLength(255)]
        public string? ContactInfo { get; set; }

        [Column(TypeName = "Date")]
        public DateTime? ApplicationDeadline { get; set; }

        [MaxLength(50)]
        public string Status { get; set; } = "Open";

        public bool IsApprovedByAdmin { get; set; } = false;

        public DateTime CreatedAt { get; set; }
        public DateTime UpdatedAt { get; set; }

        // Navigation properties
        [ForeignKey("OrganizationId")]
        public Organization? Organization { get; set; } // ĐÃ SỬA: Bỏ 'required', thêm '?' để cho phép null

        [ForeignKey("CauseId")]
        public Cause? Cause { get; set; } // ĐÃ SỬA: Bỏ 'required', thêm '?' để cho phép null

        public ICollection<OpportunitySkill> OpportunitySkills { get; set; } = new List<OpportunitySkill>();
        public ICollection<Application> Applications { get; set; } = new List<Application>();
    }
}