using HanoConnect.API.Interfaces;
using HanoConnect.API.Models;
using Microsoft.AspNetCore.Mvc;
using System.Collections.Generic;
using System.Threading.Tasks;

namespace HanoConnect.API.Controllers
{
    [Route("api/[controller]")] // Định nghĩa route cho Controller này (ví dụ: /api/Roles)
    [ApiController] // Đánh dấu đây là một API Controller
    public class RolesController : ControllerBase // Kế thừa ControllerBase
    {
        private readonly IRoleService _roleService; // Dependency Injection cho Service

        public RolesController(IRoleService roleService)
        {
            _roleService = roleService; // Gán instance của IRoleService được tiêm vào
        }

        // GET: api/Roles
        // Lấy tất cả các vai trò
        [HttpGet]
        public async Task<ActionResult<IEnumerable<Role>>> GetRoles()
        {
            var roles = await _roleService.GetAllRolesAsync();
            return Ok(roles); // Trả về 200 OK và danh sách Role
        }

        // GET: api/Roles/5
        // Lấy một vai trò theo ID
        [HttpGet("{id}")]
        public async Task<ActionResult<Role>> GetRole(int id)
        {
            var role = await _roleService.GetRoleByIdAsync(id);
            if (role == null)
            {
                return NotFound(); // Trả về 404 Not Found nếu không tìm thấy
            }
            return Ok(role); // Trả về 200 OK và đối tượng Role
        }

        // POST: api/Roles
        // Tạo một vai trò mới
        [HttpPost]
        public async Task<ActionResult<Role>> PostRole(Role role)
        {
            // Logic nghiệp vụ: kiểm tra tên vai trò đã tồn tại chưa
            var existingRole = await _roleService.GetRoleByNameAsync(role.RoleName);
            if (existingRole != null)
            {
                // Trả về 409 Conflict nếu tên vai trò đã tồn tại
                return Conflict("Role with this name already exists.");
            }

            var addedRole = await _roleService.AddRoleAsync(role);
            // Trả về 201 Created và thông tin của Role vừa tạo
            return CreatedAtAction(nameof(GetRole), new { id = addedRole?.RoleId }, addedRole);
        }

        // PUT: api/Roles/5
        // Cập nhật một vai trò theo ID
        [HttpPut("{id}")]
        public async Task<IActionResult> PutRole(int id, Role role)
        {
            if (id != role.RoleId)
            {
                return BadRequest("Role ID mismatch."); // Trả về 400 Bad Request nếu ID không khớp
            }

            var success = await _roleService.UpdateRoleAsync(role);
            if (!success)
            {
                return NotFound(); // Trả về 404 Not Found nếu không tìm thấy hoặc cập nhật thất bại
            }
            return NoContent(); // Trả về 204 No Content (thành công, không có nội dung)
        }

        // DELETE: api/Roles/5
        // Xóa một vai trò theo ID
        [HttpDelete("{id}")]
        public async Task<IActionResult> DeleteRole(int id)
        {
            var success = await _roleService.DeleteRoleAsync(id);
            if (!success)
            {
                return NotFound(); // Trả về 404 Not Found nếu không tìm thấy hoặc xóa thất bại
            }
            return NoContent(); // Trả về 204 No Content
        }
    }
}