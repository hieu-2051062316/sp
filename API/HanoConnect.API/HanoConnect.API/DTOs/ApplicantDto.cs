using System;

namespace HanoConnect.API.DTOs
{
    // DTO này chứa thông tin cần thiết để hiển thị một ứng viên trong danh sách
    public class ApplicantDto
    {
        public int ApplicationId { get; set; }
        public int VolunteerUserId { get; set; }
        public string VolunteerName { get; set; }
        public string VolunteerEmail { get; set; }
        public DateTime ApplicationTime { get; set; }
        public string CvUrl { get; set; }
        public string Status { get; set; }
    }
}
