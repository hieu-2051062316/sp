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
        public string CauseName { get; set; }

        // Navigation properties
        public ICollection<VolunteerCause> VolunteerCauses { get; set; }
        public ICollection<Opportunity> Opportunities { get; set; } // Opportunities related to this cause
    }
}