using HanoConnect.API.DTOs;
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

        // GET: api/Users/5/profile
        // Endpoint để lấy thông tin Profile của Volunteer
        [HttpGet("{id}/profile")]
        public async Task<ActionResult<VolunteerProfileDto>> GetVolunteerProfile(int id)
        {
            var profile = await _userService.GetVolunteerProfileAsync(id);
            if (profile == null)
            {
                return NotFound();
            }
            return Ok(profile);
        }

        // PUT: api/Users/5/profile
        // Endpoint để cập nhật Profile
        [HttpPut("{id}/profile")]
        public async Task<IActionResult> UpdateVolunteerProfile(int id, [FromBody] VolunteerProfileUpdateDto updateDto)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            var (success, errorMessage) = await _userService.UpdateVolunteerProfileAsync(id, updateDto);

            if (!success)
            {
                if (errorMessage != null && errorMessage.Contains("Không tìm thấy"))
                    return NotFound(new { message = errorMessage });

                return BadRequest(new { message = errorMessage });
            }

            return NoContent(); // Thành công
        }

        // POST: api/Users
        [HttpPost]
        public async Task<ActionResult<User>> PostUser(User user)
        {
            // TODO: Cần xử lý băm mật khẩu trong thực tế
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

            var success = await _userService.UpdateUserAsync(user);
            if (!success)
            {
                return NotFound();
            }
            return NoContent();
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
