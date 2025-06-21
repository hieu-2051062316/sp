namespace HanoConnect.API.Interfaces
{
    public interface IOpportunityRepository : IGenericRepository<Models.Opportunity>
    {
        // Thêm các phương thức đặc thù cho Opportunity nếu cần
        Task<IEnumerable<Models.Opportunity>> GetOpportunitiesByOrganizationIdAsync(int organizationId);
        Task<IEnumerable<Models.Opportunity>> GetOpportunitiesByCauseIdAsync(int causeId);
        Task<IEnumerable<Models.Opportunity>> SearchOpportunitiesAsync(
            string? keyword,
            int? causeId,
            int? organizationId,
            string? location,
            DateTime? startDate,
            DateTime? endDate);
        Task<Models.Opportunity?> GetOpportunityWithDetailsAsync(int id); // Để lấy cả Skill và các mối quan hệ khác
    }
}