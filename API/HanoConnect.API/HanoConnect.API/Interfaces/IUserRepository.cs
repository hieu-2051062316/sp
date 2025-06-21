namespace HanoConnect.API.Interfaces
{
    public interface IUserRepository : IGenericRepository<Models.User> // Use Models.User to avoid conflict
    {
        // Các phương thức đặc thù cho User nếu có, ví dụ:
        Task<Models.User?> GetUserByEmailAsync(string email);
    }
}