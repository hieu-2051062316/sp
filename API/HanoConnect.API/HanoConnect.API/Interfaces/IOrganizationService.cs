using HanoConnect.API.DTOs;
using HanoConnect.API.Models;
using System.Collections.Generic;
using System.Threading.Tasks;

namespace HanoConnect.API.Interfaces
{
    public interface IOrganizationService
    {
        Task<IEnumerable<Organization>> GetAllOrganizationsAsync();
        Task<Organization?> GetOrganizationByIdAsync(int id);
        Task<Organization?> AddOrganizationAsync(Organization organization);
        Task<bool> UpdateOrganizationAsync(Organization organization);
        Task<bool> DeleteOrganizationAsync(int id);
        Task<Organization?> GetOrganizationByUserIdAsync(int userId);
        Task<Organization?> GetOrganizationByNameAsync(string organizationName);
        Task<OrganizationProfileDto?> GetOrganizationProfileAsync(int organizationId);

        // Lấy danh sách các ứng viên gần đây
        Task<IEnumerable<RecentApplicantDto>> GetRecentApplicantsAsync(int organizationId, int count = 5);
    }
}
