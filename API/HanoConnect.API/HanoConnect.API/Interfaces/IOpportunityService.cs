using HanoConnect.API.Models;
using System.Collections.Generic;
using System.Threading.Tasks;

namespace HanoConnect.API.Interfaces
{
    public interface IOpportunityService
    {
        Task<IEnumerable<Opportunity>> GetAllOpportunitiesAsync();
        Task<Opportunity?> GetOpportunityByIdAsync(int id);
        Task<Opportunity?> AddOpportunityAsync(Opportunity opportunity, List<int> skillIds); // Thêm skillIds
        Task<bool> UpdateOpportunityAsync(Opportunity opportunity, List<int> skillIds); // Cập nhật skillIds
        Task<bool> DeleteOpportunityAsync(int id);
        Task<IEnumerable<Opportunity>> GetOpportunitiesByOrganizationIdAsync(int organizationId);
        Task<IEnumerable<Opportunity>> GetOpportunitiesByCauseIdAsync(int causeId);
        Task<IEnumerable<Opportunity>> SearchOpportunitiesAsync(
            string? keyword,
            int? causeId,
            int? organizationId,
            string? location,
            DateTime? startDate,
            DateTime? endDate);
    }
}