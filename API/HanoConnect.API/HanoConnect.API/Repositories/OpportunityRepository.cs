using HanoConnect.API.Data;
using HanoConnect.API.Interfaces;
using HanoConnect.API.Models;
using Microsoft.EntityFrameworkCore;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace HanoConnect.API.Repositories
{
    public class OpportunityRepository : GenericRepository<Opportunity>, IOpportunityRepository
    {
        public OpportunityRepository(ApplicationDbContext context) : base(context)
        {
        }

        public async Task<IEnumerable<Opportunity>> GetOpportunitiesByOrganizationIdAsync(int organizationId)
        {
            return await _dbSet
                .Where(o => o.OrganizationId == organizationId)
                .Include(o => o.Organization) // Tải thông tin tổ chức
                .Include(o => o.Cause)        // Tải thông tin mục đích
                .ToListAsync();
        }

        public async Task<IEnumerable<Opportunity>> GetOpportunitiesByCauseIdAsync(int causeId)
        {
            return await _dbSet
                .Where(o => o.CauseId == causeId)
                .Include(o => o.Organization)
                .Include(o => o.Cause)
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
            IQueryable<Opportunity> query = _dbSet
                .Include(o => o.Organization)
                .Include(o => o.Cause)
                .Include(o => o.OpportunitySkills)
                    .ThenInclude(os => os.Skill); // Tải kỹ năng cần thiết

            if (!string.IsNullOrWhiteSpace(keyword))
            {
                query = query.Where(o =>
                    o.Title.Contains(keyword) ||
                    (o.Description != null && o.Description.Contains(keyword)) ||
                    (o.Location != null && o.Location.Contains(keyword)) ||
                    o.Organization.OrganizationName.Contains(keyword) ||
                    o.Cause.CauseName.Contains(keyword));
            }

            if (causeId.HasValue && causeId.Value > 0)
            {
                query = query.Where(o => o.CauseId == causeId.Value);
            }

            if (organizationId.HasValue && organizationId.Value > 0)
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

        public async Task<Opportunity?> GetOpportunityWithDetailsAsync(int id)
        {
            return await _dbSet
                .Include(o => o.Organization)
                .Include(o => o.Cause)
                .Include(o => o.OpportunitySkills)
                    .ThenInclude(os => os.Skill) // Load Skills associated with the opportunity
                .FirstOrDefaultAsync(o => o.OpportunityId == id);
        }

        public override async Task<IEnumerable<Opportunity>> GetAllAsync()
        {
            // Override GetAllAsync để luôn tải các thông tin liên quan cần thiết
            return await _dbSet
                .Include(o => o.Organization)
                .Include(o => o.Cause)
                .Include(o => o.OpportunitySkills)
                    .ThenInclude(os => os.Skill)
                .ToListAsync();
        }
    }
}