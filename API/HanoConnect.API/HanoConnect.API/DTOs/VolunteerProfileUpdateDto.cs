using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;

namespace HanoConnect.API.DTOs
{
	// DTO chứa dữ liệu để cập nhật hồ sơ của Tình nguyện viên
	public class VolunteerProfileUpdateDto
	{
		[Required]
		public string FullName { get; set; }

		public string? PhoneNumber { get; set; }

		public string? District { get; set; }

		// Danh sách ID các kỹ năng và lĩnh vực quan tâm mới
		public List<int> SkillIds { get; set; } = new List<int>();
		public List<int> CauseIds { get; set; } = new List<int>();
	}
}
