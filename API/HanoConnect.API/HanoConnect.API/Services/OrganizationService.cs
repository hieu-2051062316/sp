using HanoConnect.API.Interfaces;
using HanoConnect.API.Models;
using System.Collections.Generic;
using System.Threading.Tasks;

namespace HanoConnect.API.Services
{
    public class OrganizationService : IOrganizationService
    {
        private readonly IOrganizationRepository _organizationRepository;
        private readonly IUserRepository _userRepository; // Cần để kiểm tra UserId tồn tại

        public OrganizationService(IOrganizationRepository organizationRepository, IUserRepository userRepository)
        {
            _organizationRepository = organizationRepository;
            _userRepository = userRepository;
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
            // Logic nghiệp vụ: Đảm bảo UserId liên kết tồn tại
            var userExists = await _userRepository.GetByIdAsync(organization.UserId);
            if (userExists == null)
            {
                // Bạn có thể xử lý lỗi ở đây, ví dụ: throw new ArgumentException("User does not exist.");
                // Hoặc trả về null để controller biết lỗi
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
            existingOrganization.UpdatedAt = DateTime.UtcNow; // Cập nhật thời gian

            // Kiểm tra UserId nếu nó thay đổi (hoặc luôn kiểm tra để đảm bảo tính toàn vẹn)
            if (existingOrganization.UserId != organization.UserId)
            {
                var newUserExists = await _userRepository.GetByIdAsync(organization.UserId);
                if (newUserExists == null)
                {
                    // Xử lý lỗi nếu UserId mới không tồn tại
                    return false;
                }
                existingOrganization.UserId = organization.UserId;
            }


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
    }
}