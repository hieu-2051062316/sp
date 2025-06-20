using HanoConnect.API.Models;
using System;
using System.Collections.Generic;

namespace HanoConnect.API.DTOs
{
    public class OpportunityResponseDto
    {
        public int OpportunityId { get; set; }
        public string Title { get; set; } = string.Empty;
        public string? Description { get; set; }
        public string? Location { get; set; }
        public DateTime? StartDate { get; set; }
        public DateTime? EndDate { get; set; }
        public bool IsFlexibleTime { get; set; }
        public int? RequiredVolunteers { get; set; }
        public string? Benefits { get; set; }
        public string? ContactInfo { get; set; }
        public DateTime? ApplicationDeadline { get; set; }
        public string Status { get; set; } = "Open";
        public bool IsApprovedByAdmin { get; set; }
        public DateTime CreatedAt { get; set; }
        public DateTime UpdatedAt { get; set; }

        // Thông tin liên quan từ Organization
        public int OrganizationId { get; set; }
        public string OrganizationName { get; set; } = string.Empty;
        public string? OrganizationContactPerson { get; set; }

        // Thông tin liên quan từ Cause
        public int CauseId { get; set; }
        public string CauseName { get; set; } = string.Empty;

        // Danh sách kỹ năng
        public List<SkillDto> Skills { get; set; } = new List<SkillDto>();
    }

    public class SkillDto
    {
        public int SkillId { get; set; }
        public string SkillName { get; set; } = string.Empty;
    }
}