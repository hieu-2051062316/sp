using HanoConnect.API.DTOs;
using HanoConnect.API.Interfaces;
using Microsoft.AspNetCore.Mvc;
using System.Linq; // Cần thêm để sử dụng .FirstOrDefault()
using System.Threading.Tasks;
using Microsoft.EntityFrameworkCore; // Cần thêm để sử dụng .Include()

namespace HanoConnect.API.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class AuthController : ControllerBase
    {
        private readonly IUserService _userService;
        private readonly Data.ApplicationDbContext _context; // Dùng DbContext để lấy vai trò

        public AuthController(IUserService userService, Data.ApplicationDbContext context)
        {
            _userService = userService;
            _context = context;
        }

        [HttpPost("login")]
        public async Task<IActionResult> Login([FromBody] LoginRequestDto loginRequest)
        {
            if (loginRequest == null || string.IsNullOrEmpty(loginRequest.Email))
            {
                return BadRequest("Yêu cầu không hợp lệ.");
            }

            // Tìm người dùng bằng email, đồng thời lấy cả thông tin UserRoles và Role
            var user = await _context.Users
                                     .Include(u => u.UserRoles)
                                     .ThenInclude(ur => ur.Role)
                                     .FirstOrDefaultAsync(u => u.Email == loginRequest.Email);

            if (user == null)
            {
                return Unauthorized("Email hoặc mật khẩu không đúng.");
            }

            // *** CHÚ Ý: BỎ QUA HOÀN TOÀN VIỆC KIỂM TRA MẬT KHẨU ĐỂ TEST ***
            // Trong dự án thực tế, đây là nơi sẽ kiểm tra mật khẩu đã được băm.

            var userRole = user.UserRoles?.FirstOrDefault()?.Role?.RoleName ?? "Volunteer"; // Lấy vai trò đầu tiên, hoặc mặc định là Volunteer

            var response = new LoginResponseDto
            {
                UserId = user.UserId,
                Email = user.Email,
                FullName = user.FullName,
                Role = userRole
            };

            return Ok(response);
        }
    }
}
