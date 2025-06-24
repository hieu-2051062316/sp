using System.ComponentModel.DataAnnotations;

namespace HanoConnect.API.DTOs
{
    // DTO này dùng để cập nhật trạng thái của một đơn ứng tuyển
    public class UpdateApplicationStatusDto
    {
        [Required]
        [RegularExpression("^(Accepted|Rejected)$", ErrorMessage = "Status must be 'Accepted' or 'Rejected'.")]
        public string Status { get; set; }

        // Có thể thêm trường Ghi chú của Tổ chức nếu cần
        // public string? OrganizationNotes { get; set; }
    }
}
