using HanoConnect.API.Interfaces;
using HanoConnect.API.Models;
using HanoConnect.API.DTOs;
using Microsoft.AspNetCore.Mvc;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace HanoConnect.API.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class OpportunityController : ControllerBase
    {
        private readonly IOpportunityService _opportunityService;

        public OpportunityController(IOpportunityService opportunityService)
        {
            _opportunityService = opportunityService;
        }

        // GET: api/Opportunity
        [HttpGet]
        public async Task<ActionResult<IEnumerable<Opportunity>>> GetAllOpportunities()
        {
            var opportunities = await _opportunityService.GetAllOpportunitiesAsync();
            return Ok(opportunities);
        }

        // GET: api/Opportunity/{id}
        [HttpGet("{id}")]
        public async Task<ActionResult<Opportunity>> GetOpportunityById(int id)
        {
            var opportunity = await _opportunityService.GetOpportunityByIdAsync(id);
            if (opportunity == null)
            {
                return NotFound();
            }
            return Ok(opportunity);
        }

        // POST: api/Opportunity
        [HttpPost]
        public async Task<ActionResult<Opportunity>> AddOpportunity([FromBody] OpportunityCreateDto opportunityDto)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            var newOpportunity = await _opportunityService.AddOpportunityAsync(opportunityDto);
            if (newOpportunity == null)
            {
                return BadRequest("Failed to create opportunity. Please check provided OrganizationId, CauseId, or SkillIds.");
            }
            return CreatedAtAction(nameof(GetOpportunityById), new { id = newOpportunity.OpportunityId }, newOpportunity);
        }

        // PUT: api/Opportunity/{id}
        [HttpPut("{id}")]
        public async Task<IActionResult> UpdateOpportunity(int id, [FromBody] OpportunityUpdateDto opportunityDto)
        {
            if (id != opportunityDto.OpportunityId)
            {
                return BadRequest("Opportunity ID in URL does not match ID in body.");
            }

            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            var result = await _opportunityService.UpdateOpportunityAsync(id, opportunityDto);
            if (!result)
            {
                return NotFound();
            }
            return NoContent();
        }

        // DELETE: api/Opportunity/{id}
        [HttpDelete("{id}")]
        public async Task<IActionResult> DeleteOpportunity(int id)
        {
            var result = await _opportunityService.DeleteOpportunityAsync(id);
            if (!result)
            {
                return NotFound();
            }
            return NoContent();
        }

        // GET: api/Opportunity/by-organization/{organizationId}
        [HttpGet("by-organization/{organizationId}")]
        public async Task<ActionResult<IEnumerable<Opportunity>>> GetOpportunitiesByOrganizationId(int organizationId)
        {
            var opportunities = await _opportunityService.GetOpportunitiesByOrganizationIdAsync(organizationId);
            return Ok(opportunities);
        }

        // GET: api/Opportunity/by-cause/{causeId}
        [HttpGet("by-cause/{causeId}")]
        public async Task<ActionResult<IEnumerable<Opportunity>>> GetOpportunitiesByCauseId(int causeId)
        {
            var opportunities = await _opportunityService.GetOpportunitiesByCauseIdAsync(causeId);
            return Ok(opportunities);
        }

        // GET: api/Opportunity/search
        [HttpGet("search")]
        public async Task<ActionResult<IEnumerable<Opportunity>>> SearchOpportunities(
            [FromQuery] string? keyword,
            [FromQuery] int? causeId,
            [FromQuery] int? organizationId,
            [FromQuery] string? location,
            [FromQuery] DateTime? startDate,
            [FromQuery] DateTime? endDate)
        {
            var opportunities = await _opportunityService.SearchOpportunitiesAsync(
                keyword, causeId, organizationId, location, startDate, endDate);
            return Ok(opportunities);
        }
    }
}