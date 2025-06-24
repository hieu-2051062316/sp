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
    public class OrganizationService : IOrganizationService
    {
        private readonly IOrganizationRepository _organizationRepository;
        private readonly IUserRepository _userRepository;
        private readonly ApplicationDbContext _context;

        public OrganizationService(
            IOrganizationRepository organizationRepository,
            IUserRepository userRepository,
            ApplicationDbContext context)
        {
            _organizationRepository = organizationRepository;
            _userRepository = userRepository;
            _context = context;
        }

        public async Task<IEnumerable<Organization>> GetAllOrganizationsAsync()
        {
            return await _organizationRepository.GetAllAsync();
        }

        public async Task<Organization?> GetOrganizationByIdAsync(int id)
        {
            return await _organizationRepository.GetByIdAsync(id);
        }

        public async Task<Organization?> AddOrganizationAsync(Organization organization)
        {
            var userExists = await _userRepository.GetByIdAsync(organization.UserId);
            if (userExists == null)
            {
                return null;
            }

            await _organizationRepository.AddAsync(organization);
            await _organizationRepository.SaveChangesAsync();
            return organization;
        }

        public async Task<bool> UpdateOrganizationAsync(Organization organization)
        {
            var existingOrganization = await _organizationRepository.GetByIdAsync(organization.OrganizationId);
            if (existingOrganization == null)
            {
                return false;
            }

            // Cập nhật các thuộc tính
            existingOrganization.OrganizationName = organization.OrganizationName;
            existingOrganization.ContactPerson = organization.ContactPerson;
            existingOrganization.ContactPhone = organization.ContactPhone;
            existingOrganization.Address = organization.Address;
            existingOrganization.Website = organization.Website;
            existingOrganization.Description = organization.Description;
            existingOrganization.IsVerified = organization.IsVerified;
            existingOrganization.VerifiedByAdminId = organization.VerifiedByAdminId;
            existingOrganization.VerificationTime = organization.VerificationTime;
            existingOrganization.UpdatedAt = DateTime.UtcNow;

            _organizationRepository.Update(existingOrganization);
            return await _organizationRepository.SaveChangesAsync();
        }

        public async Task<bool> DeleteOrganizationAsync(int id)
        {
            var organizationToDelete = await _organizationRepository.GetByIdAsync(id);
            if (organizationToDelete == null)
            {
                return false;
            }
            _organizationRepository.Delete(organizationToDelete);
            return await _organizationRepository.SaveChangesAsync();
        }

        public async Task<Organization?> GetOrganizationByUserIdAsync(int userId)
        {
            return await _organizationRepository.GetOrganizationByUserIdAsync(userId);
        }

        public async Task<Organization?> GetOrganizationByNameAsync(string organizationName)
        {
            return await _organizationRepository.GetOrganizationByNameAsync(organizationName);
        }

        public async Task<OrganizationProfileDto?> GetOrganizationProfileAsync(int organizationId)
        {
            var organization = await _context.Organizations
                .Where(o => o.OrganizationId == organizationId)
                .Include(o => o.User)
                .Include(o => o.Opportunities)
                    .ThenInclude(opp => opp.Applications)
                .FirstOrDefaultAsync();

            if (organization == null) return null;

            var profileDto = new OrganizationProfileDto
            {
                OrganizationId = organization.OrganizationId,
                OrganizationName = organization.OrganizationName,
                Email = organization.User?.Email,
                Description = organization.Description,
                Address = organization.Address,
                Website = organization.Website,
                TotalOpportunities = organization.Opportunities.Count,
                TotalApplications = organization.Opportunities.SelectMany(opp => opp.Applications).Count()
            };

            return profileDto;
        }

        // Lấy danh sách các ứng viên gần đây
        public async Task<IEnumerable<RecentApplicantDto>> GetRecentApplicantsAsync(int organizationId, int count = 5)
        {
            var recentApplicants = await _context.Applications
                .Where(app => app.Opportunity.OrganizationId == organizationId)
                .OrderByDescending(app => app.ApplicationTime)
                .Include(app => app.VolunteerUser)
                .Include(app => app.Opportunity)
                .Select(app => new RecentApplicantDto
                {
                    ApplicationId = app.ApplicationId,
                    VolunteerName = app.VolunteerUser.FullName,
                    OpportunityTitle = app.Opportunity.Title,
                    ApplicationTime = app.ApplicationTime
                })
                .Take(count)
                .ToListAsync();

            return recentApplicants;
        }
    }
}
