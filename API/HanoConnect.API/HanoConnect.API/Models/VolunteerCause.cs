using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace HanoConnect.API.Models
{
    [Table("VolunteerCauses")]
    public class VolunteerCause
    {
        [Key] // Primary key for this junction table
        [DatabaseGenerated(DatabaseGeneratedOption.Identity)] // Auto-incrementing identity column
        public int VolunteerCauseId { get; set; } // SQL Server uses identity column for PK

        public int UserId { get; set; }
        public int CauseId { get; set; }

        // Navigation properties
        [ForeignKey("UserId")]
        public required User User { get; set; } // Đã thêm 'required'

        [ForeignKey("CauseId")]
        public required Cause Cause { get; set; } // Đã thêm 'required'
    }
}