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
        public string SkillName { get; set; }

        // Navigation properties
        public ICollection<VolunteerSkill> VolunteerSkills { get; set; }
        public ICollection<OpportunitySkill> OpportunitySkills { get; set; }
    }
}