using HanoConnect.API.Interfaces;
using HanoConnect.API.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace HanoConnect.API.Services
{
    public class OpportunityService : IOpportunityService
    {
        private readonly IOpportunityRepository _opportunityRepository;
        private readonly IOrganizationRepository _organizationRepository; // Để kiểm tra OrganizationId
        private readonly ICauseRepository _causeRepository;             // Để kiểm tra CauseId
        private readonly ISkillRepository _skillRepository;             // Để kiểm tra SkillIds và quản lý OpportunitySkills

        public OpportunityService(
            IOpportunityRepository opportunityRepository,
            IOrganizationRepository organizationRepository,
            ICauseRepository causeRepository,
            ISkillRepository skillRepository)
        {
            _opportunityRepository = opportunityRepository;
            _organizationRepository = organizationRepository;
            _causeRepository = causeRepository;
            _skillRepository = skillRepository;
        }

        public async Task<IEnumerable<Opportunity>> GetAllOpportunitiesAsync()
        {
            return await _opportunityRepository.GetAllAsync();
        }

        public async Task<Opportunity?> GetOpportunityByIdAsync(int id)
        {
            return await _opportunityRepository.GetOpportunityWithDetailsAsync(id);
        }

        public async Task<Opportunity?> AddOpportunityAsync(Opportunity opportunity, List<int> skillIds)
        {
            // Kiểm tra OrganizationId tồn tại
            var organizationExists = await _organizationRepository.GetByIdAsync(opportunity.OrganizationId);
            if (organizationExists == null)
            {
                return null; // Hoặc throw exception tùy vào cách xử lý lỗi của bạn
            }

            // Kiểm tra CauseId tồn tại
            var causeExists = await _causeRepository.GetByIdAsync(opportunity.CauseId);
            if (causeExists == null)
            {
                return null;
            }

            // Khởi tạo OpportunitySkills nếu nó null
            opportunity.OpportunitySkills ??= new List<OpportunitySkill>();

            // Thêm các kỹ năng được yêu cầu
            foreach (var skillId in skillIds)
            {
                var skill = await _skillRepository.GetByIdAsync(skillId);
                if (skill == null)
                {
                    // Nếu có kỹ năng không tồn tại, bạn có thể xử lý lỗi hoặc bỏ qua
                    // Hiện tại sẽ trả về null để báo lỗi
                    return null;
                }
                opportunity.OpportunitySkills.Add(new OpportunitySkill { SkillId = skillId, Opportunity = opportunity });
            }

            opportunity.CreatedAt = DateTime.UtcNow;
            opportunity.UpdatedAt = DateTime.UtcNow;

            await _opportunityRepository.AddAsync(opportunity);
            await _opportunityRepository.SaveChangesAsync();
            return opportunity;
        }

        public async Task<bool> UpdateOpportunityAsync(Opportunity opportunity, List<int> skillIds)
        {
            var existingOpportunity = await _opportunityRepository.GetOpportunityWithDetailsAsync(opportunity.OpportunityId);
            if (existingOpportunity == null)
            {
                return false;
            }

            // Kiểm tra OrganizationId tồn tại (nếu thay đổi)
            if (existingOpportunity.OrganizationId != opportunity.OrganizationId)
            {
                var organizationExists = await _organizationRepository.GetByIdAsync(opportunity.OrganizationId);
                if (organizationExists == null) return false;
            }

            // Kiểm tra CauseId tồn tại (nếu thay đổi)
            if (existingOpportunity.CauseId != opportunity.CauseId)
            {
                var causeExists = await _causeRepository.GetByIdAsync(opportunity.CauseId);
                if (causeExists == null) return false;
            }

            // Cập nhật các thuộc tính cơ bản
            existingOpportunity.OrganizationId = opportunity.OrganizationId;
            existingOpportunity.CauseId = opportunity.CauseId;
            existingOpportunity.Title = opportunity.Title;
            existingOpportunity.Description = opportunity.Description;
            existingOpportunity.Location = opportunity.Location;
            existingOpportunity.StartDate = opportunity.StartDate;
            existingOpportunity.EndDate = opportunity.EndDate;
            existingOpportunity.IsFlexibleTime = opportunity.IsFlexibleTime;
            existingOpportunity.RequiredVolunteers = opportunity.RequiredVolunteers;
            existingOpportunity.Benefits = opportunity.Benefits;
            existingOpportunity.ContactInfo = opportunity.ContactInfo;
            existingOpportunity.ApplicationDeadline = opportunity.ApplicationDeadline;
            existingOpportunity.Status = opportunity.Status;
            existingOpportunity.IsApprovedByAdmin = opportunity.IsApprovedByAdmin;
            existingOpportunity.UpdatedAt = DateTime.UtcNow;

            // Cập nhật kỹ năng liên quan (OpportunitySkill)
            // Lấy các kỹ năng hiện có
            var existingSkillIds = existingOpportunity.OpportunitySkills?.Select(os => os.SkillId).ToList() ?? new List<int>();

            // Kỹ năng cần thêm
            var skillsToAdd = skillIds.Except(existingSkillIds).ToList();
            foreach (var skillId in skillsToAdd)
            {
                var skill = await _skillRepository.GetByIdAsync(skillId);
                if (skill == null) return false; // Kỹ năng không tồn tại
                existingOpportunity.OpportunitySkills?.Add(new OpportunitySkill { OpportunityId = existingOpportunity.OpportunityId, SkillId = skillId });
            }

            // Kỹ năng cần xóa
            var skillsToRemove = existingSkillIds.Except(skillIds).ToList();
            if (existingOpportunity.OpportunitySkills != null)
            {
                foreach (var skillId in skillsToRemove)
                {
                    var osToRemove = existingOpportunity.OpportunitySkills.FirstOrDefault(os => os.SkillId == skillId);
                    if (osToRemove != null)
                    {
                        // Đã thay đổi thành _opportunityRepository.Delete(osToRemove);
                        _opportunityRepository.Delete(osToRemove);
                    }
                }
            }

            _opportunityRepository.Update(existingOpportunity); // Update opportunity itself
            return await _opportunityRepository.SaveChangesAsync();
        }

        public async Task<bool> DeleteOpportunityAsync(int id)
        {
            var opportunityToDelete = await _opportunityRepository.GetByIdAsync(id);
            if (opportunityToDelete == null)
            {
                return false;
            }
            _opportunityRepository.Delete(opportunityToDelete);
            return await _opportunityRepository.SaveChangesAsync();
        }

        public async Task<IEnumerable<Opportunity>> GetOpportunitiesByOrganizationIdAsync(int organizationId)
        {
            return await _opportunityRepository.GetOpportunitiesByOrganizationIdAsync(organizationId);
        }

        public async Task<IEnumerable<Opportunity>> GetOpportunitiesByCauseIdAsync(int causeId)
        {
            return await _opportunityRepository.GetOpportunitiesByCauseIdAsync(causeId);
        }

        public async Task<IEnumerable<Opportunity>> SearchOpportunitiesAsync(
            string? keyword,
            int? causeId,
            int? organizationId,
            string? location,
            DateTime? startDate,
            DateTime? endDate)
        {
            return await _opportunityRepository.SearchOpportunitiesAsync(keyword, causeId, organizationId, location, startDate, endDate);
        }
    }
}