namespace HanoConnect.API.Interfaces
{
    public interface ISkillRepository : IGenericRepository<Models.Skill>
    {
        // Thêm phương thức đặc thù để lấy Skill theo tên
        Task<Models.Skill?> GetSkillByNameAsync(string skillName);
    }
}