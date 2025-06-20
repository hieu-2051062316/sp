using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;

namespace HanoConnect.API.DTOs
{
    public class OpportunityUpdateDto
    {
        [Required(ErrorMessage = "Opportunity ID is required for update.")]
        public int OpportunityId { get; set; } // Cần ID để biết cơ hội nào đang được cập nhật

        [Required(ErrorMessage = "Organization ID is required.")]
        public int OrganizationId { get; set; }

        [Required(ErrorMessage = "Title is required.")]
        [MaxLength(255, ErrorMessage = "Title cannot exceed 255 characters.")]
        public string Title { get; set; } = string.Empty;

        [Required(ErrorMessage = "Description is required.")]
        public string Description { get; set; } = string.Empty;

        [Required(ErrorMessage = "Cause ID is required.")]
        public int CauseId { get; set; }

        [MaxLength(255, ErrorMessage = "Location cannot exceed 255 characters.")]
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

        // Danh sách các SkillId được cập nhật cho cơ hội này
        public List<int> SkillIds { get; set; } = new List<int>();
    }
}