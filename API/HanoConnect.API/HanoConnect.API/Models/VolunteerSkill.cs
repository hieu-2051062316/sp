using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace HanoConnect.API.Models
{
    [Table("VolunteerSkills")]
    public class VolunteerSkill
    {
        [Key] // Primary key for this junction table
        [DatabaseGenerated(DatabaseGeneratedOption.Identity)] // Auto-incrementing identity column
        public int VolunteerSkillId { get; set; } // SQL Server uses identity column for PK

        public int UserId { get; set; }
        public int SkillId { get; set; }

        // Navigation properties
        [ForeignKey("UserId")]
        public required User User { get; set; } // Thêm 'required'

        [ForeignKey("SkillId")]
        public required Skill Skill { get; set; } // Thêm 'required'
    }
}