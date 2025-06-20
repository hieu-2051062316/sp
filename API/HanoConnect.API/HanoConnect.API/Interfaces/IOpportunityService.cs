using HanoConnect.API.Models;
using HanoConnect.API.DTOs;
using System.Collections.Generic;
using System.Threading.Tasks;
using System;

namespace HanoConnect.API.Interfaces
{
    public interface IOpportunityService
    {
        Task<IEnumerable<Opportunity>> GetAllOpportunitiesAsync();
        Task<Opportunity?> GetOpportunityByIdAsync(int id);
        Task<Opportunity?> AddOpportunityAsync(OpportunityCreateDto opportunityDto);
        Task<bool> UpdateOpportunityAsync(int id, OpportunityUpdateDto opportunityDto);
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