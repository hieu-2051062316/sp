namespace HanoConnect.API.Interfaces
{
    public interface IRoleRepository : IGenericRepository<Models.Role>
    {
        // Thêm các phương thức đặc thù cho Role nếu có.
        // Ví dụ, chúng ta muốn tìm một Role theo tên của nó.
        Task<Models.Role?> GetRoleByNameAsync(string roleName);
    }
}