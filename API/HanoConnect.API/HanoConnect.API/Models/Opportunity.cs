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
        public string Title { get; set; } // Không cần sửa, đã có [Required]

        // [Required] // <--- Xóa [Required] nếu Description có thể null trong DB
        [Column(TypeName = "NVARCHAR(MAX)")] // NVARCHAR(MAX)
        public string? Description { get; set; } // <--- Thêm '?' và xem xét xóa [Required] ở trên

        public int CauseId { get; set; }

        [MaxLength(255)]
        public string? Location { get; set; } // <--- Thêm '?'

        public DateTime? StartDate { get; set; } // Đã là nullable, không cần sửa
        public DateTime? EndDate { get; set; } // Đã là nullable, không cần sửa

        public bool IsFlexibleTime { get; set; } = false; // bool là non-nullable, đã có giá trị mặc định, không cần sửa

        public int? RequiredVolunteers { get; set; } // Đã là nullable, không cần sửa

        [Column(TypeName = "NVARCHAR(MAX)")] // NVARCHAR(MAX)
        public string? Benefits { get; set; } // <--- Thêm '?'

        [MaxLength(255)]
        public string? ContactInfo { get; set; } // <--- Thêm '?'

        [Column(TypeName = "Date")] // DATE type
        public DateTime? ApplicationDeadline { get; set; } // Đã là nullable, không cần sửa

        [MaxLength(50)]
        public string Status { get; set; } = "Open"; // Đã có giá trị mặc định, không cần sửa

        public bool IsApprovedByAdmin { get; set; } = false; // bool là non-nullable, đã có giá trị mặc định, không cần sửa

        public DateTime CreatedAt { get; set; } // DateTime là non-nullable, thường được gán giá trị khi tạo đối tượng. Không cần sửa nếu bạn gán nó khi khởi tạo.
        public DateTime UpdatedAt { get; set; } // Tương tự CreatedAt

        // Navigation properties
        [ForeignKey("OrganizationId")]
        public required Organization Organization { get; set; } // <--- Thêm 'required'

        [ForeignKey("CauseId")]
        public required Cause Cause { get; set; } // <--- Thêm 'required'

        // Khắc phục: Khởi tạo các collection
        public ICollection<OpportunitySkill> OpportunitySkills { get; set; } = new List<OpportunitySkill>();
        public ICollection<Application> Applications { get; set; } = new List<Application>();
    }
}