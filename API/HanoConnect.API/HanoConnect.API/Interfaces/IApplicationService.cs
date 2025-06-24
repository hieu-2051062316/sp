using HanoConnect.API.DTOs;
using HanoConnect.API.Models;
using System.Collections.Generic;
using System.Threading.Tasks;

namespace HanoConnect.API.Interfaces
{
    public interface IApplicationService
    {
        Task<(Application? application, string? errorMessage)> CreateApplicationAsync(ApplyDto applyDto);
        Task<IEnumerable<ApplicantDto>> GetApplicantsForOpportunityAsync(int opportunityId);

        // Cập nhật trạng thái của một đơn ứng tuyển
        Task<bool> UpdateApplicationStatusAsync(int applicationId, string newStatus);

        // Lấy danh sách các đơn đã nộp của một tình nguyện viên
        Task<IEnumerable<MyApplicationDto>> GetApplicationsByVolunteerIdAsync(int volunteerUserId);
    }
}
