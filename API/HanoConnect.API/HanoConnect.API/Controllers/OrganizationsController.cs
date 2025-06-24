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
    public class OrganizationsController : ControllerBase
    {
        private readonly IOrganizationService _organizationService;
        private readonly IUserService _userService;

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

        // GET: api/Organizations/5/profile
        [HttpGet("{id}/profile")]
        public async Task<ActionResult<OrganizationProfileDto>> GetOrganizationProfile(int id)
        {
            var profile = await _organizationService.GetOrganizationProfileAsync(id);
            if (profile == null)
            {
                return NotFound();
            }
            return Ok(profile);
        }

        // GET: api/Organizations/5/recent-applicants
        [HttpGet("{id}/recent-applicants")]
        public async Task<ActionResult<IEnumerable<RecentApplicantDto>>> GetRecentApplicants(int id)
        {
            var applicants = await _organizationService.GetRecentApplicantsAsync(id);
            return Ok(applicants);
        }

        // GET: api/Organizations/byuser/1
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

        // POST: api/Organizations
        [HttpPost]
        public async Task<ActionResult<Organization>> PostOrganization(Organization organization)
        {
            var userExists = await _userService.GetUserByIdAsync(organization.UserId);
            if (userExists == null)
            {
                return BadRequest("Associated User ID does not exist.");
            }

            var addedOrganization = await _organizationService.AddOrganizationAsync(organization);
            if (addedOrganization == null)
            {
                return BadRequest("Could not add organization.");
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
    }
}
