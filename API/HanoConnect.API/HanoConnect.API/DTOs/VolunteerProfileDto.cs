using System;
using System.Collections.Generic;

namespace HanoConnect.API.DTOs
{
    // DTO chứa thông tin chi tiết cho trang hồ sơ của Tình nguyện viên
    public class VolunteerProfileDto
    {
        public int UserId { get; set; }
        public string FullName { get; set; }
        public string Email { get; set; }
        public string PhoneNumber { get; set; }
        public DateTime? DateOfBirth { get; set; }
        public string District { get; set; }

        public List<string> Skills { get; set; } = new List<string>();
        public List<string> Causes { get; set; } = new List<string>();
    }
}
