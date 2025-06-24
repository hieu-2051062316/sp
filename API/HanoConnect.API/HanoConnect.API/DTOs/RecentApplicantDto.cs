using System;

namespace HanoConnect.API.DTOs
{
    // DTO cho các ứng viên gần đây trên Dashboard
    public class RecentApplicantDto
    {
        public int ApplicationId { get; set; }
        public string VolunteerName { get; set; }
        public string OpportunityTitle { get; set; }
        public DateTime ApplicationTime { get; set; }
    }
}
