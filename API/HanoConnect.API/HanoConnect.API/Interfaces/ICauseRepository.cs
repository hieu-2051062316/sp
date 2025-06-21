namespace HanoConnect.API.Interfaces
{
    public interface ICauseRepository : IGenericRepository<Models.Cause>
    {
        // Các phương thức đặc thù cho Cause nếu có
        Task<Models.Cause?> GetCauseByNameAsync(string causeName);
    }
}