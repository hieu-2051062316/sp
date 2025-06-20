using HanoConnect.API.Interfaces;
using HanoConnect.API.Models;
using System.Collections.Generic;
using System.Threading.Tasks;

namespace HanoConnect.API.Services
{
    public class SkillService : ISkillService
    {
        private readonly ISkillRepository _skillRepository;

        public SkillService(ISkillRepository skillRepository)
        {
            _skillRepository = skillRepository;
        }

        public async Task<IEnumerable<Skill>> GetAllSkillsAsync()
        {
            return await _skillRepository.GetAllAsync();
        }

        public async Task<Skill?> GetSkillByIdAsync(int id)
        {
            return await _skillRepository.GetByIdAsync(id);
        }

        public async Task<Skill?> AddSkillAsync(Skill skill)
        {
            await _skillRepository.AddAsync(skill);
            await _skillRepository.SaveChangesAsync();
            return skill;
        }

        public async Task<bool> UpdateSkillAsync(Skill skill)
        {
            var existingSkill = await _skillRepository.GetByIdAsync(skill.SkillId);
            if (existingSkill == null)
            {
                return false;
            }

            existingSkill.SkillName = skill.SkillName;
            // Thêm bất kỳ thuộc tính nào khác có thể được cập nhật

            _skillRepository.Update(existingSkill);
            return await _skillRepository.SaveChangesAsync();
        }

        public async Task<bool> DeleteSkillAsync(int id)
        {
            var skillToDelete = await _skillRepository.GetByIdAsync(id);
            if (skillToDelete == null)
            {
                return false;
            }
            _skillRepository.Delete(skillToDelete);
            return await _skillRepository.SaveChangesAsync();
        }

        public async Task<Skill?> GetSkillByNameAsync(string skillName)
        {
            return await _skillRepository.GetSkillByNameAsync(skillName);
        }
    }
}