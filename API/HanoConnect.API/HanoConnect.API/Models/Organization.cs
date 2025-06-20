using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using System.Text.Json.Serialization; 

namespace HanoConnect.API.Models
{
    [Table("Organizations")]
    public class Organization
    {
        [Key]
        [DatabaseGenerated(DatabaseGeneratedOption.Identity)]
        public int OrganizationId { get; set; }

        // Khóa ngoại, UserId là bắt buộc để liên kết với User
        public int UserId { get; set; } // FK to Users table (Unique in DB)

        [Required]
        [MaxLength(255)]
        public required string OrganizationName { get; set; }

        [MaxLength(255)]
        public string? ContactPerson { get; set; }

        [MaxLength(20)]
        public string? ContactPhone { get; set; }

        [MaxLength(255)]
        public string? Address { get; set; }

        [MaxLength(255)]
        public string? Website { get; set; }

        [Column(TypeName = "NVARCHAR(MAX)")] // Maps to SQL Server NVARCHAR(MAX)
        public string? Description { get; set; }

        public bool IsVerified { get; set; }

        public int? VerifiedByAdminId { get; set; }
        public DateTime? VerificationTime { get; set; }

        public DateTime CreatedAt { get; set; }
        public DateTime UpdatedAt { get; set; }

        // Navigation properties
        [ForeignKey("UserId")]
        [JsonIgnore] // Thêm thuộc tính này để bỏ qua khi deserialize từ request body
        public User? User { get; set; }

        [ForeignKey("VerifiedByAdminId")]
        [JsonIgnore]
        public User? VerifiedByAdmin { get; set; }

        [JsonIgnore] 
        public ICollection<Opportunity>? Opportunities { get; set; } = new List<Opportunity>();

        [JsonIgnore]
        public ICollection<Feedback>? ReceivedFeedbacks { get; set; } = new List<Feedback>();
    }
}