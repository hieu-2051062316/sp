using HanoConnect.API.Interfaces;
using HanoConnect.API.Models;
using Microsoft.AspNetCore.Mvc;
using System.Collections.Generic;
using System.Threading.Tasks;

namespace HanoConnect.API.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class UsersController : ControllerBase
    {
        private readonly IUserService _userService;

        public UsersController(IUserService userService)
        {
            _userService = userService;
        }

        // GET: api/Users
        [HttpGet]
        public async Task<ActionResult<IEnumerable<User>>> GetUsers()
        {
            var users = await _userService.GetAllUsersAsync();
            return Ok(users);
        }

        // GET: api/Users/5
        [HttpGet("{id}")]
        public async Task<ActionResult<User>> GetUser(int id)
        {
            var user = await _userService.GetUserByIdAsync(id);
            if (user == null)
            {
                return NotFound();
            }
            return Ok(user);
        }

        // POST: api/Users
        // Để đơn giản hóa, chúng ta sẽ thêm User trực tiếp. Trong thực tế cần có DTOs và xử lý băm mật khẩu
        [HttpPost]
        public async Task<ActionResult<User>> PostUser(User user)
        {
            // TODO: Trong ứng dụng thực tế, cần băm mật khẩu trước khi lưu vào DB.
            // Ví dụ: user.PasswordHash = BCrypt.Net.BCrypt.HashPassword(user.PasswordHash);
            // Hoặc bạn sẽ có một DTO cho việc đăng ký và xử lý băm mật khẩu trong Service.

            var addedUser = await _userService.AddUserAsync(user);
            return CreatedAtAction(nameof(GetUser), new { id = addedUser?.UserId }, addedUser);
        }

        // PUT: api/Users/5
        [HttpPut("{id}")]
        public async Task<IActionResult> PutUser(int id, User user)
        {
            if (id != user.UserId)
            {
                return BadRequest("User ID mismatch.");
            }

            // TODO: Trong ứng dụng thực tế, cần xử lý cẩn thận việc cập nhật mật khẩu.
            // Có thể không cho phép cập nhật mật khẩu qua API này hoặc yêu cầu mật khẩu cũ.

            var success = await _userService.UpdateUserAsync(user);
            if (!success)
            {
                return NotFound(); // Hoặc lỗi chi tiết hơn nếu không tìm thấy hoặc lỗi cập nhật khác
            }
            return NoContent(); // 204 No Content - thành công nhưng không có nội dung trả về
        }

        // DELETE: api/Users/5
        [HttpDelete("{id}")]
        public async Task<IActionResult> DeleteUser(int id)
        {
            var success = await _userService.DeleteUserAsync(id);
            if (!success)
            {
                return NotFound();
            }
            return NoContent();
        }
    }
}