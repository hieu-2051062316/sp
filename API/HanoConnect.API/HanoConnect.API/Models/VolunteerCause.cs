using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace HanoConnect.API.Models
{
    [Table("VolunteerCauses")]
    public class VolunteerCause
    {
        [Key]
        [DatabaseGenerated(DatabaseGeneratedOption.Identity)]
        public int VolunteerCauseId { get; set; }

        public int UserId { get; set; }
        public int CauseId { get; set; }

        // Sửa lỗi: Cho phép null và bỏ 'required'
        [ForeignKey("UserId")]
        public User? User { get; set; }

        [ForeignKey("CauseId")]
        public Cause? Cause { get; set; }
    }
}
