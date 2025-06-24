namespace HanoConnect.API.DTOs
{
    public class LoginResponseDto
    {
        public int UserId { get; set; }
        public string Email { get; set; }
        public string FullName { get; set; }
        public string Role { get; set; }
        // public string Token { get; set; } // Sẽ thêm vào sau nếu cần

        // --- BẮT ĐẦU THÊM MỚI ---
        // ID của tổ chức, có thể là null nếu người dùng là Volunteer hoặc Admin
        public int? OrganizationId { get; set; }
        // --- KẾT THÚC THÊM MỚI ---
    }
}
