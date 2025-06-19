using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace HanoConnect.API.Models
{
    [Table("OpportunitySkills")]
    public class OpportunitySkill
    {
        [Key] // Primary key for this junction table
        [DatabaseGenerated(DatabaseGeneratedOption.Identity)] // Auto-incrementing identity column
        public int OpportunitySkillId { get; set; } // SQL Server uses identity column for PK

        public int OpportunityId { get; set; }
        public int SkillId { get; set; }

        // Navigation properties
        [ForeignKey("OpportunityId")]
        public Opportunity Opportunity { get; set; }

        [ForeignKey("SkillId")]
        public Skill Skill { get; set; }
    }
}