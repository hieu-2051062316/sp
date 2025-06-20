using HanoConnect.API.Models;
using HanoConnect.API.DTOs; // Thêm dòng này
using System.Collections.Generic;
using System.Threading.Tasks;
using System;

namespace HanoConnect.API.Interfaces
{
    public interface IOpportunityService
    {
        Task<IEnumerable<OpportunityResponseDto>> GetAllOpportunitiesAsync(); // Thay đổi kiểu trả về
        Task<OpportunityResponseDto?> GetOpportunityByIdAsync(int id); // Thay đổi kiểu trả về
        Task<Opportunity?> AddOpportunityAsync(OpportunityCreateDto opportunityDto);
        Task<bool> UpdateOpportunityAsync(int id, OpportunityUpdateDto opportunityDto);
        Task<bool> DeleteOpportunityAsync(int id);
        Task<IEnumerable<OpportunityResponseDto>> GetOpportunitiesByOrganizationIdAsync(int organizationId); // Thay đổi kiểu trả về
        Task<IEnumerable<OpportunityResponseDto>> GetOpportunitiesByCauseIdAsync(int causeId); // Thay đổi kiểu trả về
        Task<IEnumerable<OpportunityResponseDto>> SearchOpportunitiesAsync( // Thay đổi kiểu trả về
            string? keyword,
            int? causeId,
            int? organizationId,
            string? location,
            DateTime? startDate,
            DateTime? endDate);
    }
}