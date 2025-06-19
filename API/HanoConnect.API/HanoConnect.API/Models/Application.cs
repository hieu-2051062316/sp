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
        public string? MotivationLetter { get; set; } // <--- Thêm '?' nếu trường này có thể null trong DB

        [MaxLength(50)]
        public string Status { get; set; } = "Pending"; // Đã xử lý bằng giá trị mặc định, không cần sửa

        [Column(TypeName = "NVARCHAR(MAX)")] // NVARCHAR(MAX)
        public string? OrganizationNotes { get; set; } // <--- Thêm '?' nếu trường này có thể null trong DB

        // Navigation properties
        [ForeignKey("OpportunityId")]
        public required Opportunity Opportunity { get; set; } // <--- Thêm 'required'

        [ForeignKey("VolunteerUserId")]
        public required User VolunteerUser { get; set; } // <--- Thêm 'required'

        // Feedback can be associated with an application (nullable FK in DB)
        // public Feedback Feedback { get; set; } // Nếu thuộc tính này đang bị comment, nó sẽ không gây cảnh báo.
        // Nếu bạn bỏ comment và Feedback là nullable, hãy thêm '?':
        // public Feedback? Feedback { get; set; }
    }
}