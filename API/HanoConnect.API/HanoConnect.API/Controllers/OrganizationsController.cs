using HanoConnect.API.Interfaces;
using HanoConnect.API.Models;
using Microsoft.AspNetCore.Mvc;
using System.Collections.Generic;
using System.Threading.Tasks;

namespace HanoConnect.API.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class OrganizationsController : ControllerBase
    {
        private readonly IOrganizationService _organizationService;
        private readonly IUserService _userService; // Để kiểm tra sự tồn tại của AdminId và UserId

        public OrganizationsController(IOrganizationService organizationService, IUserService userService)
        {
            _organizationService = organizationService;
            _userService = userService;
        }

        // GET: api/Organizations
        [HttpGet]
        public async Task<ActionResult<IEnumerable<Organization>>> GetOrganizations()
        {
            var organizations = await _organizationService.GetAllOrganizationsAsync();
            return Ok(organizations);
        }

        // GET: api/Organizations/5
        [HttpGet("{id}")]
        public async Task<ActionResult<Organization>> GetOrganization(int id)
        {
            var organization = await _organizationService.GetOrganizationByIdAsync(id);
            if (organization == null)
            {
                return NotFound();
            }
            return Ok(organization);
        }

        // POST: api/Organizations
        [HttpPost]
        public async Task<ActionResult<Organization>> PostOrganization(Organization organization)
        {
            // Kiểm tra UserId có tồn tại không
            var userExists = await _userService.GetUserByIdAsync(organization.UserId);
            if (userExists == null)
            {
                return BadRequest("Associated User ID does not exist.");
            }

            // Kiểm tra OrganizationName đã tồn tại chưa để tránh trùng lặp
            var existingOrgByName = await _organizationService.GetOrganizationByNameAsync(organization.OrganizationName);
            if (existingOrgByName != null)
            {
                return Conflict("Organization with this name already exists.");
            }

            // Kiểm tra xem UserId này đã được gán cho một tổ chức khác chưa
            var existingOrgByUserId = await _organizationService.GetOrganizationByUserIdAsync(organization.UserId);
            if (existingOrgByUserId != null)
            {
                return Conflict("This User ID is already associated with another organization.");
            }

            // Đảm bảo các giá trị mặc định nếu không được cung cấp từ client
            organization.CreatedAt = DateTime.UtcNow;
            organization.UpdatedAt = DateTime.UtcNow;
            // isVerified và VerificationTime sẽ được xử lý khi admin xác minh

            var addedOrganization = await _organizationService.AddOrganizationAsync(organization);

            // Nếu AddOrganizationAsync trả về null (do logic kiểm tra bên trong service, ví dụ User không tồn tại)
            if (addedOrganization == null)
            {
                return BadRequest("Could not add organization. Check user ID and other details.");
            }

            return CreatedAtAction(nameof(GetOrganization), new { id = addedOrganization.OrganizationId }, addedOrganization);
        }

        // PUT: api/Organizations/5
        [HttpPut("{id}")]
        public async Task<IActionResult> PutOrganization(int id, Organization organization)
        {
            if (id != organization.OrganizationId)
            {
                return BadRequest("Organization ID mismatch.");
            }

            // Kiểm tra UserId có tồn tại không (nếu nó thay đổi hoặc để đảm bảo)
            var userExists = await _userService.GetUserByIdAsync(organization.UserId);
            if (userExists == null)
            {
                return BadRequest("Associated User ID does not exist.");
            }

            // Kiểm tra VerifiedByAdminId có tồn tại không (nếu có và khác null)
            if (organization.VerifiedByAdminId.HasValue)
            {
                var adminUserExists = await _userService.GetUserByIdAsync(organization.VerifiedByAdminId.Value);
                if (adminUserExists == null)
                {
                    return BadRequest("Verified By Admin ID does not exist.");
                }
            }


            // Kiểm tra OrganizationName trùng lặp, trừ trường hợp đó chính là bản thân tổ chức đang được cập nhật
            var existingOrgByName = await _organizationService.GetOrganizationByNameAsync(organization.OrganizationName);
            if (existingOrgByName != null && existingOrgByName.OrganizationId != id)
            {
                return Conflict("Organization with this name already exists.");
            }

            // Kiểm tra User ID trùng lặp, trừ trường hợp đó chính là bản thân tổ chức đang được cập nhật
            var existingOrgByUserId = await _organizationService.GetOrganizationByUserIdAsync(organization.UserId);
            if (existingOrgByUserId != null && existingOrgByUserId.OrganizationId != id)
            {
                return Conflict("This User ID is already associated with another organization.");
            }

            var success = await _organizationService.UpdateOrganizationAsync(organization);
            if (!success)
            {
                return NotFound("Organization not found or update failed.");
            }
            return NoContent();
        }

        // DELETE: api/Organizations/5
        [HttpDelete("{id}")]
        public async Task<IActionResult> DeleteOrganization(int id)
        {
            var success = await _organizationService.DeleteOrganizationAsync(id);
            if (!success)
            {
                return NotFound();
            }
            return NoContent();
        }

        // GET: api/Organizations/byuser/1
        // Lấy tổ chức theo UserId
        [HttpGet("byuser/{userId}")]
        public async Task<ActionResult<Organization>> GetOrganizationByUserId(int userId)
        {
            var organization = await _organizationService.GetOrganizationByUserIdAsync(userId);
            if (organization == null)
            {
                return NotFound("Organization not found for this User ID.");
            }
            return Ok(organization);
        }
    }
}