using HanoConnect.API.Data;
using HanoConnect.API.DTOs;
using HanoConnect.API.Models;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using System;
using System.Threading.Tasks;

namespace HanoConnect.API.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class ApplicationsController : ControllerBase
    {
        private readonly ApplicationDbContext _context;

        public ApplicationsController(ApplicationDbContext context)
        {
            _context = context;
        }

        [HttpPost("apply")]
        public async Task<IActionResult> CreateApplication([FromBody] ApplyDto applyDto)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            var existingApplication = await _context.Applications
                .FirstOrDefaultAsync(a => a.VolunteerUserId == applyDto.VolunteerUserId && a.OpportunityId == applyDto.OpportunityId);

            if (existingApplication != null)
            {
                return Conflict("Bạn đã ứng tuyển vào cơ hội này rồi.");
            }

            var application = new Application
            {
                OpportunityId = applyDto.OpportunityId,
                VolunteerUserId = applyDto.VolunteerUserId,
                MotivationLetter = applyDto.MotivationLetter,
                CvUrl = applyDto.CvUrl,
                ApplicationTime = DateTime.UtcNow,
                Status = "Pending"
            };

            _context.Applications.Add(application);
            await _context.SaveChangesAsync();

            return CreatedAtAction(nameof(GetApplication), new { id = application.ApplicationId }, application);
        }

        [HttpGet("{id}")]
        public async Task<ActionResult<Application>> GetApplication(int id)
        {
            var application = await _context.Applications.FindAsync(id);

            if (application == null)
            {
                return NotFound();
            }

            return application;
        }
    }
}
