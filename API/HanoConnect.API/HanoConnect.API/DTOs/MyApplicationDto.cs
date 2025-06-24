using System;

namespace HanoConnect.API.DTOs
{
    // DTO này chứa thông tin về một đơn ứng tuyển từ góc nhìn của Tình nguyện viên
    public class MyApplicationDto
    {
        public int ApplicationId { get; set; }
        public int OpportunityId { get; set; }
        public string OpportunityTitle { get; set; }
        public string OrganizationName { get; set; }
        public string Status { get; set; } // Trạng thái đơn: Pending, Accepted, Rejected
        public DateTime ApplicationTime { get; set; }
    }
}
