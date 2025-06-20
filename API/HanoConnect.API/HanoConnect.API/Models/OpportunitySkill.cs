using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using System.Text.Json.Serialization;

namespace HanoConnect.API.Models
{
    [Table("OpportunitySkills")]
    public class OpportunitySkill
    {
        [Key]
        [DatabaseGenerated(DatabaseGeneratedOption.Identity)]
        public int OpportunitySkillId { get; set; }

        public int OpportunityId { get; set; }
        public int SkillId { get; set; }

        // Navigation properties
        [JsonIgnore]
        [ForeignKey("OpportunityId")]
        public Opportunity? Opportunity { get; set; } // THÊM '?'

        [JsonIgnore]
        [ForeignKey("SkillId")]
        public Skill? Skill { get; set; } // THÊM '?'
    }
}