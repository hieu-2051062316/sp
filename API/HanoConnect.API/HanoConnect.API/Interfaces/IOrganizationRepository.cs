namespace HanoConnect.API.Interfaces
{
    public interface IOrganizationRepository : IGenericRepository<Models.Organization>
    {
        // Thêm các phương thức đặc thù cho Organization nếu cần
        Task<Models.Organization?> GetOrganizationByUserIdAsync(int userId);
        Task<Models.Organization?> GetOrganizationByNameAsync(string organizationName);
    }
}