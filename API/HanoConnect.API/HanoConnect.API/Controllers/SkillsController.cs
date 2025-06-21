using HanoConnect.API.Interfaces;
using HanoConnect.API.Models;
using Microsoft.AspNetCore.Mvc;
using System.Collections.Generic;
using System.Threading.Tasks;

namespace HanoConnect.API.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class SkillsController : ControllerBase
    {
        private readonly ISkillService _skillService;

        public SkillsController(ISkillService skillService)
        {
            _skillService = skillService;
        }

        // GET: api/Skills
        [HttpGet]
        public async Task<ActionResult<IEnumerable<Skill>>> GetSkills()
        {
            var skills = await _skillService.GetAllSkillsAsync();
            return Ok(skills);
        }

        // GET: api/Skills/5
        [HttpGet("{id}")]
        public async Task<ActionResult<Skill>> GetSkill(int id)
        {
            var skill = await _skillService.GetSkillByIdAsync(id);
            if (skill == null)
            {
                return NotFound();
            }
            return Ok(skill);
        }

        // POST: api/Skills
        [HttpPost]
        public async Task<ActionResult<Skill>> PostSkill(Skill skill)
        {
            // Kiểm tra xem SkillName đã tồn tại chưa để tránh trùng lặp
            var existingSkill = await _skillService.GetSkillByNameAsync(skill.SkillName);
            if (existingSkill != null)
            {
                return Conflict("Skill with this name already exists."); // Trả về mã lỗi 409 Conflict
            }

            var addedSkill = await _skillService.AddSkillAsync(skill);
            return CreatedAtAction(nameof(GetSkill), new { id = addedSkill?.SkillId }, addedSkill);
        }

        // PUT: api/Skills/5
        [HttpPut("{id}")]
        public async Task<IActionResult> PutSkill(int id, Skill skill)
        {
            if (id != skill.SkillId)
            {
                return BadRequest("Skill ID mismatch.");
            }

            var success = await _skillService.UpdateSkillAsync(skill);
            if (!success)
            {
                return NotFound();
            }
            return NoContent();
        }

        // DELETE: api/Skills/5
        [HttpDelete("{id}")]
        public async Task<IActionResult> DeleteSkill(int id)
        {
            var success = await _skillService.DeleteSkillAsync(id);
            if (!success)
            {
                return NotFound();
            }
            return NoContent();
        }
    }
}