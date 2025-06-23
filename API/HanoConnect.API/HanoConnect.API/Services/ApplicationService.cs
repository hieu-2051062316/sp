using HanoConnect.API.Data; // Cần để truy cập DbContext
using HanoConnect.API.DTOs;
using HanoConnect.API.Interfaces;
using HanoConnect.API.Models;
using Microsoft.EntityFrameworkCore; // Cần cho Include và Select
using System;
using System.Collections.Generic; // Cần cho IEnumerable
using System.Linq; // Cần cho Select
using System.Threading.Tasks;

namespace HanoConnect.API.Services
{
    public class ApplicationService : IApplicationService
    {
        private readonly IApplicationRepository _applicationRepository;
        private readonly ApplicationDbContext _context; // Inject DbContext để join bảng

        public ApplicationService(IApplicationRepository applicationRepository, ApplicationDbContext context)
        {
            _applicationRepository = applicationRepository;
            _context = context;
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
            await _applicationRepository.SaveChangesAsync();

            return (application, null);
        }

        // Lấy danh sách ứng viên cho một cơ hội
        public async Task<IEnumerable<ApplicantDto>> GetApplicantsForOpportunityAsync(int opportunityId)
        {
            var applicants = await _context.Applications
                .Where(a => a.OpportunityId == opportunityId)
                .Include(a => a.VolunteerUser) // Join với bảng Users để lấy thông tin người dùng
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
    }
}
