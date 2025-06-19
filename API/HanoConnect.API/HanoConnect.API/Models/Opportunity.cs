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
        public required string Title { get; set; } // <--- ĐÃ SỬA: Thêm từ khóa 'required' ở đây

        // [Required] // <--- Giữ nguyên khuyến nghị: Xóa [Required] nếu Description có thể null trong DB
        [Column(TypeName = "NVARCHAR(MAX)")] // NVARCHAR(MAX)
        public string? Description { get; set; } // Đã thêm '?'

        public int CauseId { get; set; }

        [MaxLength(255)]
        public string? Location { get; set; } // Đã thêm '?'

        public DateTime? StartDate { get; set; }
        public DateTime? EndDate { get; set; }

        public bool IsFlexibleTime { get; set; } = false;

        public int? RequiredVolunteers { get; set; }

        [Column(TypeName = "NVARCHAR(MAX)")] // NVARCHAR(MAX)
        public string? Benefits { get; set; } // Đã thêm '?'

        [MaxLength(255)]
        public string? ContactInfo { get; set; } // Đã thêm '?'

        [Column(TypeName = "Date")] // DATE type
        public DateTime? ApplicationDeadline { get; set; }

        [MaxLength(50)]
        public string Status { get; set; } = "Open";

        public bool IsApprovedByAdmin { get; set; } = false;

        public DateTime CreatedAt { get; set; }
        public DateTime UpdatedAt { get; set; }

        // Navigation properties
        [ForeignKey("OrganizationId")]
        public required Organization Organization { get; set; } // Đã thêm 'required'

        [ForeignKey("CauseId")]
        public required Cause Cause { get; set; } // Đã thêm 'required'

        // Khắc phục: Khởi tạo các collection
        public ICollection<OpportunitySkill> OpportunitySkills { get; set; } = new List<OpportunitySkill>();
        public ICollection<Application> Applications { get; set; } = new List<Application>();
    }
}