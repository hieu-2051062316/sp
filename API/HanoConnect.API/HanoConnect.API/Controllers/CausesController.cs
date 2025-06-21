using HanoConnect.API.Interfaces;
using HanoConnect.API.Models;
using Microsoft.AspNetCore.Mvc;
using System.Collections.Generic;
using System.Threading.Tasks;

namespace HanoConnect.API.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class CausesController : ControllerBase
    {
        private readonly ICauseService _causeService;

        public CausesController(ICauseService causeService)
        {
            _causeService = causeService;
        }

        // GET: api/Causes
        [HttpGet]
        public async Task<ActionResult<IEnumerable<Cause>>> GetCauses()
        {
            var causes = await _causeService.GetAllCausesAsync();
            return Ok(causes);
        }

        // GET: api/Causes/5
        [HttpGet("{id}")]
        public async Task<ActionResult<Cause>> GetCause(int id)
        {
            var cause = await _causeService.GetCauseByIdAsync(id);
            if (cause == null)
            {
                return NotFound();
            }
            return Ok(cause);
        }

        // POST: api/Causes
        [HttpPost]
        public async Task<ActionResult<Cause>> PostCause(Cause cause)
        {
            // Kiểm tra xem CauseName đã tồn tại chưa để tránh trùng lặp
            var existingCause = await _causeService.GetCauseByNameAsync(cause.CauseName);
            if (existingCause != null)
            {
                return Conflict("Cause with this name already exists."); // Trả về mã lỗi 409 Conflict
            }

            var addedCause = await _causeService.AddCauseAsync(cause);
            return CreatedAtAction(nameof(GetCause), new { id = addedCause?.CauseId }, addedCause);
        }

        // PUT: api/Causes/5
        [HttpPut("{id}")]
        public async Task<IActionResult> PutCause(int id, Cause cause)
        {
            if (id != cause.CauseId)
            {
                return BadRequest("Cause ID mismatch.");
            }

            var success = await _causeService.UpdateCauseAsync(cause);
            if (!success)
            {
                return NotFound();
            }
            return NoContent();
        }

        // DELETE: api/Causes/5
        [HttpDelete("{id}")]
        public async Task<IActionResult> DeleteCause(int id)
        {
            var success = await _causeService.DeleteCauseAsync(id);
            if (!success)
            {
                return NotFound();
            }
            return NoContent();
        }
    }
}