using HanoConnect.API.Interfaces;
using HanoConnect.API.Models;
using HanoConnect.API.Data;
using HanoConnect.API.DTOs;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.EntityFrameworkCore;

namespace HanoConnect.API.Services
{
    public class OpportunityService : IOpportunityService
    {
        private readonly IOpportunityRepository _opportunityRepository;
        private readonly IOrganizationRepository _organizationRepository;
        private readonly ICauseRepository _causeRepository;
        private readonly ISkillRepository _skillRepository;
        private readonly ApplicationDbContext _context;

        public OpportunityService(
            IOpportunityRepository opportunityRepository,
            IOrganizationRepository organizationRepository,
            ICauseRepository causeRepository,
            ISkillRepository skillRepository,
            ApplicationDbContext context)
        {
            _opportunityRepository = opportunityRepository;
            _organizationRepository = organizationRepository;
            _causeRepository = causeRepository;
            _skillRepository = skillRepository;
            _context = context;
        }

        public async Task<IEnumerable<Opportunity>> GetAllOpportunitiesAsync()
        {
            return await _context.Opportunities
                                 .Include(o => o.Organization)
                                 .Include(o => o.Cause)
                                 .Include(o => o.OpportunitySkills)
                                     .ThenInclude(os => os.Skill)
                                 .ToListAsync();
        }

        public async Task<Opportunity?> GetOpportunityByIdAsync(int id)
        {
            return await _context.Opportunities
                                 .Include(o => o.Organization)
                                 .Include(o => o.Cause)
                                 .Include(o => o.OpportunitySkills)
                                     .ThenInclude(os => os.Skill)
                                 .FirstOrDefaultAsync(o => o.OpportunityId == id);
        }

        public async Task<Opportunity?> AddOpportunityAsync(OpportunityCreateDto opportunityDto)
        {
            // Kiểm tra OrganizationId tồn tại và lấy đối tượng Organization
            var organization = await _organizationRepository.GetByIdAsync(opportunityDto.OrganizationId);
            if (organization == null)
            {
                return null;
            }

            // Kiểm tra CauseId tồn tại và lấy đối tượng Cause
            var cause = await _causeRepository.GetByIdAsync(opportunityDto.CauseId);
            if (cause == null)
            {
                return null;
            }

            var opportunity = new Opportunity
            {
                OrganizationId = opportunityDto.OrganizationId,
                Organization = organization, // Gán đối tượng Organization
                Title = opportunityDto.Title,
                Description = opportunityDto.Description,
                CauseId = opportunityDto.CauseId,
                Cause = cause, // Gán đối tượng Cause
                Location = opportunityDto.Location,
                StartDate = opportunityDto.StartDate,
                EndDate = opportunityDto.EndDate,
                IsFlexibleTime = opportunityDto.IsFlexibleTime,
                RequiredVolunteers = opportunityDto.RequiredVolunteers,
                Benefits = opportunityDto.Benefits,
                ContactInfo = opportunityDto.ContactInfo,
                ApplicationDeadline = opportunityDto.ApplicationDeadline,
                Status = opportunityDto.Status,
                IsApprovedByAdmin = opportunityDto.IsApprovedByAdmin,
                CreatedAt = DateTime.UtcNow,
                UpdatedAt = DateTime.UtcNow,
                OpportunitySkills = new List<OpportunitySkill>()
            };

            foreach (var skillId in opportunityDto.SkillIds)
            {
                var skill = await _skillRepository.GetByIdAsync(skillId);
                if (skill == null)
                {
                    return null;
                }
                opportunity.OpportunitySkills.Add(new OpportunitySkill { SkillId = skillId, Opportunity = opportunity });
            }

            await _opportunityRepository.AddAsync(opportunity);
            await _opportunityRepository.SaveChangesAsync();

            return opportunity;
        }

        public async Task<bool> UpdateOpportunityAsync(int id, OpportunityUpdateDto opportunityDto)
        {
            if (id != opportunityDto.OpportunityId)
            {
                return false;
            }

            var existingOpportunity = await _context.Opportunities
                                                   .Include(o => o.OpportunitySkills)
                                                   .FirstOrDefaultAsync(o => o.OpportunityId == id);

            if (existingOpportunity == null)
            {
                return false;
            }

            // Kiểm tra OrganizationId và CauseId tồn tại nếu chúng thay đổi
            if (existingOpportunity.OrganizationId != opportunityDto.OrganizationId)
            {
                var organizationExists = await _organizationRepository.GetByIdAsync(opportunityDto.OrganizationId);
                if (organizationExists == null) return false;
            }

            if (existingOpportunity.CauseId != opportunityDto.CauseId)
            {
                var causeExists = await _causeRepository.GetByIdAsync(opportunityDto.CauseId);
                if (causeExists == null) return false;
            }

            existingOpportunity.OrganizationId = opportunityDto.OrganizationId;
            existingOpportunity.Title = opportunityDto.Title;
            existingOpportunity.Description = opportunityDto.Description;
            existingOpportunity.CauseId = opportunityDto.CauseId;
            existingOpportunity.Location = opportunityDto.Location;
            existingOpportunity.StartDate = opportunityDto.StartDate;
            existingOpportunity.EndDate = opportunityDto.EndDate;
            existingOpportunity.IsFlexibleTime = opportunityDto.IsFlexibleTime;
            existingOpportunity.RequiredVolunteers = opportunityDto.RequiredVolunteers;
            existingOpportunity.Benefits = opportunityDto.Benefits;
            existingOpportunity.ContactInfo = opportunityDto.ContactInfo;
            existingOpportunity.ApplicationDeadline = opportunityDto.ApplicationDeadline;
            existingOpportunity.Status = opportunityDto.Status;
            existingOpportunity.IsApprovedByAdmin = opportunityDto.IsApprovedByAdmin;
            existingOpportunity.UpdatedAt = DateTime.UtcNow;

            var currentSkillIds = existingOpportunity.OpportunitySkills?.Select(os => os.SkillId).ToList() ?? new List<int>();

            var skillsToAdd = opportunityDto.SkillIds.Except(currentSkillIds).ToList();
            foreach (var skillId in skillsToAdd)
            {
                var skill = await _skillRepository.GetByIdAsync(skillId);
                if (skill == null) return false;
                existingOpportunity.OpportunitySkills?.Add(new OpportunitySkill { OpportunityId = existingOpportunity.OpportunityId, SkillId = skillId });
            }

            var skillsToRemove = currentSkillIds.Except(opportunityDto.SkillIds).ToList();
            if (existingOpportunity.OpportunitySkills != null)
            {
                foreach (var skillId in skillsToRemove)
                {
                    var osToRemove = existingOpportunity.OpportunitySkills.FirstOrDefault(os => os.SkillId == skillId);
                    if (osToRemove != null)
                    {
                        _context.OpportunitySkills.Remove(osToRemove);
                    }
                }
            }

            _opportunityRepository.Update(existingOpportunity);
            return await _opportunityRepository.SaveChangesAsync();
        }

        public async Task<bool> DeleteOpportunityAsync(int id)
        {
            var opportunityToDelete = await _context.Opportunities
                                                     .Include(o => o.OpportunitySkills)
                                                     .FirstOrDefaultAsync(o => o.OpportunityId == id);
            if (opportunityToDelete == null)
            {
                return false;
            }

            if (opportunityToDelete.OpportunitySkills != null && opportunityToDelete.OpportunitySkills.Count > 0) // Thay thế .Any() bằng .Count > 0
            {
                _context.OpportunitySkills.RemoveRange(opportunityToDelete.OpportunitySkills);
            }

            _opportunityRepository.Delete(opportunityToDelete);
            return await _opportunityRepository.SaveChangesAsync();
        }

        public async Task<IEnumerable<Opportunity>> GetOpportunitiesByOrganizationIdAsync(int organizationId)
        {
            return await _context.Opportunities
                                 .Include(o => o.Organization)
                                 .Include(o => o.Cause)
                                 .Include(o => o.OpportunitySkills)
                                     .ThenInclude(os => os.Skill)
                                 .Where(o => o.OrganizationId == organizationId)
                                 .ToListAsync();
        }

        public async Task<IEnumerable<Opportunity>> GetOpportunitiesByCauseIdAsync(int causeId)
        {
            return await _context.Opportunities
                                 .Include(o => o.Organization)
                                 .Include(o => o.Cause)
                                 .Include(o => o.OpportunitySkills)
                                     .ThenInclude(os => os.Skill)
                                 .Where(o => o.CauseId == causeId)
                                 .ToListAsync();
        }

        public async Task<IEnumerable<Opportunity>> SearchOpportunitiesAsync(
            string? keyword,
            int? causeId,
            int? organizationId,
            string? location,
            DateTime? startDate,
            DateTime? endDate)
        {
            var query = _context.Opportunities
                                .Include(o => o.Organization)
                                .Include(o => o.Cause)
                                .Include(o => o.OpportunitySkills)
                                    .ThenInclude(os => os.Skill)
                                .AsQueryable();

            if (!string.IsNullOrWhiteSpace(keyword))
            {
                // Đảm bảo o.Description không null trước khi gọi Contains
                query = query.Where(o => o.Title.Contains(keyword) || (o.Description != null && o.Description.Contains(keyword)));
            }

            if (causeId.HasValue)
            {
                query = query.Where(o => o.CauseId == causeId.Value);
            }

            if (organizationId.HasValue)
            {
                query = query.Where(o => o.OrganizationId == organizationId.Value);
            }

            if (!string.IsNullOrWhiteSpace(location))
            {
                query = query.Where(o => o.Location != null && o.Location.Contains(location));
            }

            if (startDate.HasValue)
            {
                query = query.Where(o => o.StartDate >= startDate.Value);
            }

            if (endDate.HasValue)
            {
                query = query.Where(o => o.EndDate <= endDate.Value);
            }

            return await query.ToListAsync();
        }
    }
}