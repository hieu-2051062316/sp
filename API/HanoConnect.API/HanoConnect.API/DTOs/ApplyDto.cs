using System.ComponentModel.DataAnnotations;

namespace HanoConnect.API.DTOs
{
    public class ApplyDto
    {
        [Required]
        public int OpportunityId { get; set; }

        [Required]
        public int VolunteerUserId { get; set; }

        public string? MotivationLetter { get; set; }

        [Required]
        [Url]
        public string CvUrl { get; set; }
    }
}