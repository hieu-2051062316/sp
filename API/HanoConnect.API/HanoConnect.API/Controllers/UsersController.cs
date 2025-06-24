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

        // Endpoint mới để cập nhật Profile
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
                // Trả về 404 nếu không tìm thấy, hoặc 400 cho các lỗi khác
                if (errorMessage != null && errorMessage.Contains("Không tìm thấy"))
                    return NotFound(new { message = errorMessage });

                return BadRequest(new { message = errorMessage });
            }

            return NoContent(); // Thành công
        }

        // ... các endpoint khác giữ nguyên ...
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
    }
}
