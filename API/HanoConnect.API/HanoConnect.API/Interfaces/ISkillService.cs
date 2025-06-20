using HanoConnect.API.Models;
using System.Collections.Generic;
using System.Threading.Tasks;

namespace HanoConnect.API.Interfaces
{
    public interface ISkillService
    {
        Task<IEnumerable<Skill>> GetAllSkillsAsync();
        Task<Skill?> GetSkillByIdAsync(int id);
        Task<Skill?> AddSkillAsync(Skill skill);
        Task<bool> UpdateSkillAsync(Skill skill);
        Task<bool> DeleteSkillAsync(int id);
        Task<Skill?> GetSkillByNameAsync(string skillName);
    }
}