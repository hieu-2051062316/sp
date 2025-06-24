using HanoConnect.API.Interfaces;
using HanoConnect.API.Models;

namespace HanoConnect.API.Interfaces
{
    public interface IApplicationRepository : IGenericRepository<Application>
    {
        // Tìm một đơn ứng tuyển dựa trên user và opportunity
        Task<Application?> FindByUserAndOpportunityAsync(int volunteerUserId, int opportunityId);
    }
}