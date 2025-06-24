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

        // Đã loại bỏ INotificationService khỏi constructor
        public ApplicationService(IApplicationRepository applicationRepository, ApplicationDbContext context)
        {
            _applicationRepository = applicationRepository;
            _context = context;
        }

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

            // Không còn logic tạo thông báo
            return (application, null);
        }

        public async Task<bool> UpdateApplicationStatusAsync(int applicationId, string newStatus)
        {
            var application = await _applicationRepository.GetByIdAsync(applicationId);
            if (application == null)
            {
                return false;
            }

            application.Status = newStatus;
            _applicationRepository.Update(application);

            // Không còn logic tạo thông báo
            return await _applicationRepository.SaveChangesAsync();
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

        public async Task<IEnumerable<MyApplicationDto>> GetApplicationsByVolunteerIdAsync(int volunteerUserId)
        {
            var myApplications = await _context.Applications
                .Where(a => a.VolunteerUserId == volunteerUserId)
                .Include(a => a.Opportunity)
                    .ThenInclude(o => o.Organization)
                .Select(a => new MyApplicationDto
                {
                    ApplicationId = a.ApplicationId,
                    OpportunityId = a.OpportunityId,
                    OpportunityTitle = a.Opportunity.Title,
                    OrganizationName = a.Opportunity.Organization.OrganizationName,
                    Status = a.Status,
                    ApplicationTime = a.ApplicationTime
                })
                .OrderByDescending(a => a.ApplicationTime)
                .ToListAsync();

            return myApplications;
        }
    }
}
