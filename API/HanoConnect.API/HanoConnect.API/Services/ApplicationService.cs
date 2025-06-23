using HanoConnect.API.Data;
using HanoConnect.API.DTOs;
using HanoConnect.API.Interfaces;
using HanoConnect.API.Models;
using Microsoft.EntityFrameworkCore;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace HanoConnect.API.Services
{
    public class ApplicationService : IApplicationService
    {
        private readonly IApplicationRepository _applicationRepository;
        private readonly ApplicationDbContext _context;

        public ApplicationService(IApplicationRepository applicationRepository, ApplicationDbContext context)
        {
            _applicationRepository = applicationRepository;
            _context = context;
        }

        // ... (hàm CreateApplicationAsync và GetApplicantsForOpportunityAsync giữ nguyên)

        public async Task<(Application? application, string? errorMessage)> CreateApplicationAsync(ApplyDto applyDto)
        {
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
            await _applicationRepository.SaveChangesAsync();

            return (application, null);
        }

        public async Task<IEnumerable<ApplicantDto>> GetApplicantsForOpportunityAsync(int opportunityId)
        {
            var applicants = await _context.Applications
                .Where(a => a.OpportunityId == opportunityId)
                .Include(a => a.VolunteerUser)
                .Select(a => new ApplicantDto
                {
                    ApplicationId = a.ApplicationId,
                    VolunteerUserId = a.VolunteerUserId,
                    VolunteerName = a.VolunteerUser.FullName,
                    VolunteerEmail = a.VolunteerUser.Email,
                    ApplicationTime = a.ApplicationTime,
                    CvUrl = a.CvUrl,
                    Status = a.Status
                })
                .ToListAsync();

            return applicants;
        }

        // Hàm cập nhật trạng thái đơn
        public async Task<bool> UpdateApplicationStatusAsync(int applicationId, string newStatus)
        {
            var application = await _applicationRepository.GetByIdAsync(applicationId);
            if (application == null)
            {
                return false; // Không tìm thấy đơn
            }

            application.Status = newStatus;
            // Cập nhật thêm các trường khác nếu cần, ví dụ: OrganizationNotes
            _applicationRepository.Update(application);

            return await _applicationRepository.SaveChangesAsync();
        }
    }
}
