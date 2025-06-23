using HanoConnect.API.DTOs;
using HanoConnect.API.Interfaces;
using HanoConnect.API.Models;
using Microsoft.AspNetCore.Mvc;
using System.Collections.Generic; // Cần cho IEnumerable
using System.Linq;
using System.Threading.Tasks;

namespace HanoConnect.API.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class ApplicationsController : ControllerBase
    {
        private readonly IApplicationService _applicationService;

        public ApplicationsController(IApplicationService applicationService)
        {
            _applicationService = applicationService;
        }

        // Endpoint mới để lấy danh sách ứng viên
        [HttpGet("opportunity/{opportunityId}")]
        public async Task<ActionResult<IEnumerable<ApplicantDto>>> GetApplicants(int opportunityId)
        {
            var applicants = await _applicationService.GetApplicantsForOpportunityAsync(opportunityId);
            return Ok(applicants);
        }

        [HttpPost("apply")]
        public async Task<IActionResult> CreateApplication([FromBody] ApplyDto applyDto)
        {
            if (!ModelState.IsValid)
            {
                var errors = ModelState.Values.SelectMany(v => v.Errors)
                                              .Select(e => e.ErrorMessage);
                return BadRequest(new
                {
                    message = "Dữ liệu gửi lên không hợp lệ.",
                    errors = errors
                });
            }

            var (application, errorMessage) = await _applicationService.CreateApplicationAsync(applyDto);

            if (application == null)
            {
                if (errorMessage != null && errorMessage.Contains("đã ứng tuyển"))
                {
                    return Conflict(new { message = errorMessage });
                }
                return BadRequest(new { message = errorMessage ?? "Không thể tạo đơn ứng tuyển." });
            }

            return StatusCode(201, application);
        }

        [HttpGet("{id}")]
        public async Task<ActionResult<Application>> GetApplication(int id)
        {
            // TODO: Triển khai logic lấy Application bằng Service
            return Ok(new { Message = $"Placeholder for getting application with ID {id}." });
        }
    }
}
