using HanoConnect.API.DTOs;
using HanoConnect.API.Models;
using System.Collections.Generic; // Cần cho IEnumerable
using System.Threading.Tasks;

namespace HanoConnect.API.Interfaces
{
    public interface IApplicationService
    {
        // Trả về một tuple chứa application hoặc một thông báo lỗi
        Task<(Application? application, string? errorMessage)> CreateApplicationAsync(ApplyDto applyDto);

        // Lấy danh sách ứng viên cho một cơ hội
        Task<IEnumerable<ApplicantDto>> GetApplicantsForOpportunityAsync(int opportunityId);
    }
}
