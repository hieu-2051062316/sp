using HanoConnect.API.DTOs;
using HanoConnect.API.Interfaces;
using Microsoft.AspNetCore.Mvc;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.EntityFrameworkCore;

namespace HanoConnect.API.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class AuthController : ControllerBase
    {
        private readonly IUserService _userService;
        private readonly Data.ApplicationDbContext _context;

        public AuthController(IUserService userService, Data.ApplicationDbContext context)
        {
            _userService = userService;
            _context = context;
        }

        [HttpPost("register")]
        public async Task<IActionResult> Register([FromBody] RegisterRequestDto registerDto)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            var (user, errorMessage) = await _userService.RegisterUserAsync(registerDto);

            if (user == null)
            {
                // Trả về lỗi 400 Bad Request nếu có lỗi từ service
                return BadRequest(new { message = errorMessage });
            }

            // Trả về 201 Created khi đăng ký thành công
            return StatusCode(201, new { message = "Đăng ký thành công." });
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

            // Tạm thời bỏ qua kiểm tra mật khẩu để test
            var userRole = user.UserRoles?.FirstOrDefault()?.Role?.RoleName ?? "Volunteer";

            var response = new LoginResponseDto
            {
                UserId = user.UserId,
                Email = user.Email,
                FullName = user.FullName,
                Role = userRole
            };

            // Nếu người dùng là một tổ chức, tìm và đính kèm OrganizationId
            if (userRole.Equals("Organization", System.StringComparison.OrdinalIgnoreCase))
            {
                var organization = await _context.Organizations
                                                 .FirstOrDefaultAsync(o => o.UserId == user.UserId);
                if (organization != null)
                {
                    response.OrganizationId = organization.OrganizationId;
                }
            }

            return Ok(response);
        }
    }
}
