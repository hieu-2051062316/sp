using HanoConnect.API.Interfaces;
using HanoConnect.API.Models;
using System.Collections.Generic;
using System.Threading.Tasks;

namespace HanoConnect.API.Services
{
    public class RoleService : IRoleService
    {
        private readonly IRoleRepository _roleRepository; // Dependency Injection cho Repository

        public RoleService(IRoleRepository roleRepository)
        {
            _roleRepository = roleRepository; // Gán instance của RoleRepository được tiêm vào
        }

        public async Task<IEnumerable<Role>> GetAllRolesAsync()
        {
            return await _roleRepository.GetAllAsync(); // Gọi phương thức từ Repository
        }

        public async Task<Role?> GetRoleByIdAsync(int id)
        {
            return await _roleRepository.GetByIdAsync(id);
        }

        public async Task<Role?> AddRoleAsync(Role role)
        {
            // Logic nghiệp vụ: thêm Role vào database
            await _roleRepository.AddAsync(role);
            // Lưu thay đổi vào database
            await _roleRepository.SaveChangesAsync();
            return role; // Trả về Role đã được thêm
        }

        public async Task<bool> UpdateRoleAsync(Role role)
        {
            // Logic nghiệp vụ: kiểm tra sự tồn tại trước khi cập nhật
            var existingRole = await _roleRepository.GetByIdAsync(role.RoleId);
            if (existingRole == null)
            {
                return false; // Không tìm thấy Role để cập nhật
            }

            // Cập nhật các thuộc tính của Role hiện có
            existingRole.RoleName = role.RoleName;
            // Nếu có các thuộc tính khác có thể cập nhật, hãy thêm vào đây

            _roleRepository.Update(existingRole); // Đánh dấu là đã thay đổi
            return await _roleRepository.SaveChangesAsync(); // Lưu thay đổi
        }

        public async Task<bool> DeleteRoleAsync(int id)
        {
            // Logic nghiệp vụ: kiểm tra sự tồn tại trước khi xóa
            var roleToDelete = await _roleRepository.GetByIdAsync(id);
            if (roleToDelete == null)
            {
                return false; // Không tìm thấy Role để xóa
            }
            _roleRepository.Delete(roleToDelete); // Đánh dấu là cần xóa
            return await _roleRepository.SaveChangesAsync(); // Lưu thay đổi
        }

        public async Task<Role?> GetRoleByNameAsync(string roleName)
        {
            return await _roleRepository.GetRoleByNameAsync(roleName);
        }
    }
}