using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace HanoConnect.API.Models
{
    [Table("VolunteerSkills")]
    public class VolunteerSkill
    {
        [Key]
        [DatabaseGenerated(DatabaseGeneratedOption.Identity)]
        public int VolunteerSkillId { get; set; }

        public int UserId { get; set; }
        public int SkillId { get; set; }

        // Sửa lỗi: Cho phép null và bỏ 'required'
        [ForeignKey("UserId")]
        public User? User { get; set; }

        [ForeignKey("SkillId")]
        public Skill? Skill { get; set; }
    }
}
