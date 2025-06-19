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
        public string OrganizationName { get; set; }

        [MaxLength(255)]
        public string? ContactPerson { get; set; } // <--- Thêm '?'

        [MaxLength(20)]
        public string? ContactPhone { get; set; } // <--- Thêm '?'

        [MaxLength(255)]
        public string? Address { get; set; } // <--- Thêm '?'

        [MaxLength(255)]
        public string? Website { get; set; } // <--- Thêm '?'

        [Column(TypeName = "NVARCHAR(MAX)")] // Maps to SQL Server NVARCHAR(MAX)
        public string? Description { get; set; } // <--- Thêm '?'

        public bool IsVerified { get; set; } // BIT DEFAULT 0 (bool là non-nullable type, mặc định là false, không gây cảnh báo)

        public int? VerifiedByAdminId { get; set; } // Nullable FK to Admin User
        public DateTime? VerificationTime { get; set; } // Nullable DateTime (đã là nullable rồi, không cần sửa)

        public DateTime CreatedAt { get; set; } // DateTime là non-nullable, nếu không được gán, sẽ có giá trị mặc định. Nếu bạn muốn nó là nullable, thêm '?'. Tuy nhiên, CreatedAt thường không null.
        public DateTime UpdatedAt { get; set; } // Tương tự CreatedAt

        // Navigation properties
        [ForeignKey("UserId")]
        public required User User { get; set; } // <--- Thêm 'required'

        [ForeignKey("VerifiedByAdminId")]
        public User? VerifiedByAdmin { get; set; } // <--- Thêm '?'

        public ICollection<Opportunity> Opportunities { get; set; } = new List<Opportunity>(); // <--- Khởi tạo
        public ICollection<Feedback> ReceivedFeedbacks { get; set; } = new List<Feedback>(); // <--- Khởi tạo
    }
}