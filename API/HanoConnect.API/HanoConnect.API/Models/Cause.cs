using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace HanoConnect.API.Models
{
    [Table("Causes")]
    public class Cause
    {
        [Key]
        [DatabaseGenerated(DatabaseGeneratedOption.Identity)]
        public int CauseId { get; set; }

        [Required]
        [MaxLength(100)]
        public required string CauseName { get; set; } // Đã thêm từ khóa 'required'

        // Navigation properties
        public ICollection<VolunteerCause> VolunteerCauses { get; set; } = new List<VolunteerCause>();
        public ICollection<Opportunity> Opportunities { get; set; } = new List<Opportunity>();
    }
}