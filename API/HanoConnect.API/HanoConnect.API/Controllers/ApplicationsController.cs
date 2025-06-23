using HanoConnect.API.DTOs;
using HanoConnect.API.Interfaces;
using HanoConnect.API.Models;
using Microsoft.AspNetCore.Mvc;
using System.Collections.Generic;
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

        [HttpGet("opportunity/{opportunityId}")]
        public async Task<ActionResult<IEnumerable<ApplicantDto>>> GetApplicants(int opportunityId)
        {
            var applicants = await _applicationService.GetApplicantsForOpportunityAsync(opportunityId);
            return Ok(applicants);
        }

        // Endpoint mới để cập nhật trạng thái (Duyệt/Từ chối)
        [HttpPut("{applicationId}/status")]
        public async Task<IActionResult> UpdateStatus(int applicationId, [FromBody] UpdateApplicationStatusDto statusDto)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            var success = await _applicationService.UpdateApplicationStatusAsync(applicationId, statusDto.Status);

            if (!success)
            {
                return NotFound(new { message = "Không tìm thấy đơn ứng tuyển." });
            }

            return NoContent(); // Trả về 204 No Content khi thành công
        }

        [HttpPost("apply")]
        public async Task<IActionResult> CreateApplication([FromBody] ApplyDto applyDto)
        {
            if (!ModelState.IsValid)
            {
                var errors = ModelState.Values.SelectMany(v => v.Errors).Select(e => e.ErrorMessage);
                return BadRequest(new { message = "Dữ liệu gửi lên không hợp lệ.", errors = errors });
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
