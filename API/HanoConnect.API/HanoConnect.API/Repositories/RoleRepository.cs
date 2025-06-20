using HanoConnect.API.Data;
using HanoConnect.API.Interfaces;
using HanoConnect.API.Models;
using Microsoft.EntityFrameworkCore; // Để sử dụng .FirstOrDefaultAsync()

namespace HanoConnect.API.Repositories
{
    public class RoleRepository : GenericRepository<Role>, IRoleRepository
    {
        public RoleRepository(ApplicationDbContext context) : base(context)
        {
            // Constructor này sẽ nhận ApplicationDbContext và truyền nó lên lớp cha (GenericRepository).
            // Lớp cha sẽ xử lý việc thiết lập _context và _dbSet.
        }

        // Triển khai phương thức đặc thù GetRoleByNameAsync từ IRoleRepository
        public async Task<Role?> GetRoleByNameAsync(string roleName)
        {
            return await _dbSet.FirstOrDefaultAsync(r => r.RoleName == roleName);
        }
    }
}