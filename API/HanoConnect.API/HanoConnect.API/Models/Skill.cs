using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace HanoConnect.API.Models
{
    [Table("Skills")]
    public class Skill
    {
        [Key]
        [DatabaseGenerated(DatabaseGeneratedOption.Identity)]
        public int SkillId { get; set; }

        [Required]
        [MaxLength(100)]
        public required string SkillName { get; set; } // Đã thêm từ khóa 'required'

        // Navigation properties
        public ICollection<VolunteerSkill> VolunteerSkills { get; set; } = new List<VolunteerSkill>();
        public ICollection<OpportunitySkill> OpportunitySkills { get; set; } = new List<OpportunitySkill>();
    }
}