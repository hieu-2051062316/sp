using System;

namespace HanoConnect.API.DTOs
{
    // DTO chứa thông tin chi tiết cho trang hồ sơ của Tổ chức
    public class OrganizationProfileDto
    {
        public int OrganizationId { get; set; }
        public string OrganizationName { get; set; }
        public string Email { get; set; } // Email của tài khoản user liên kết
        public string Description { get; set; }
        public string Address { get; set; }
        public string Website { get; set; }
        public int TotalOpportunities { get; set; }
        public int TotalApplications { get; set; }
    }
}
