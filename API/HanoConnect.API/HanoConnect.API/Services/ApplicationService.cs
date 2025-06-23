using HanoConnect.API.DTOs;
using HanoConnect.API.Interfaces;
using HanoConnect.API.Models;
using System;
using System.Threading.Tasks;

namespace HanoConnect.API.Services
{
    public class ApplicationService : IApplicationService
    {
        private readonly IApplicationRepository _applicationRepository;
        // Chúng ta có thể inject các repo khác nếu cần kiểm tra sâu hơn

        public ApplicationService(IApplicationRepository applicationRepository)
        {
            _applicationRepository = applicationRepository;
        }

        public async Task<(Application? application, string? errorMessage)> CreateApplicationAsync(ApplyDto applyDto)
        {
            // Kiểm tra xem người dùng đã ứng tuyển cơ hội này chưa
            var existingApplication = await _applicationRepository.FindByUserAndOpportunityAsync(applyDto.VolunteerUserId, applyDto.OpportunityId);
            if (existingApplication != null)
            {
                return (null, "Bạn đã ứng tuyển vào cơ hội này rồi.");
            }

            var application = new Application
            {
                OpportunityId = applyDto.OpportunityId,
                VolunteerUserId = applyDto.VolunteerUserId,
                MotivationLetter = applyDto.MotivationLetter,
                CvUrl = applyDto.CvUrl,
                ApplicationTime = DateTime.UtcNow,
                Status = "Pending"
            };

            await _applicationRepository.AddAsync(application);
            // Việc lưu thay đổi sẽ được thực hiện ở đây
            await _applicationRepository.SaveChangesAsync();

            // Trả về application đã được tạo và không có lỗi
            return (application, null);
        }
    }
}
