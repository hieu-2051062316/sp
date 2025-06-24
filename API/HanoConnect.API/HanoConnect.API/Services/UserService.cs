using HanoConnect.API.Data;
using HanoConnect.API.DTOs;
using HanoConnect.API.Interfaces;
using HanoConnect.API.Models;
using Microsoft.EntityFrameworkCore;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace HanoConnect.API.Services
{
    public class UserService : IUserService
    {
        private readonly IUserRepository _userRepository;
        private readonly IRoleRepository _roleRepository;
        private readonly IOrganizationRepository _organizationRepository;
        private readonly INotificationService _notificationService;
        private readonly ApplicationDbContext _context;

        public UserService(
            IUserRepository userRepository,
            ApplicationDbContext context,
            IRoleRepository roleRepository,
            IOrganizationRepository organizationRepository,
            INotificationService notificationService)
        {
            _userRepository = userRepository;
            _context = context;
            _roleRepository = roleRepository;
            _organizationRepository = organizationRepository;
            _notificationService = notificationService;
        }

        public async Task<IEnumerable<User>> GetAllUsersAsync()
        {
            return await _userRepository.GetAllAsync();
        }

        public async Task<User?> GetUserByIdAsync(int id)
        {
            return await _userRepository.GetByIdAsync(id);
        }

        public async Task<User?> AddUserAsync(User user)
        {
            await _userRepository.AddAsync(user);
            await _userRepository.SaveChangesAsync();
            return user;
        }

        public async Task<bool> UpdateUserAsync(User user)
        {
            var existingUser = await _userRepository.GetByIdAsync(user.UserId);
            if (existingUser == null)
            {
                return false;
            }

            existingUser.Email = user.Email;
            existingUser.PasswordHash = user.PasswordHash;
            existingUser.FullName = user.FullName;
            existingUser.PhoneNumber = user.PhoneNumber;
            existingUser.DateOfBirth = user.DateOfBirth;
            existingUser.District = user.District;

            _userRepository.Update(existingUser);
            return await _userRepository.SaveChangesAsync();
        }

        public async Task<bool> DeleteUserAsync(int id)
        {
            var userToDelete = await _userRepository.GetByIdAsync(id);
            if (userToDelete == null)
            {
                return false;
            }
            _userRepository.Delete(userToDelete);
            return await _userRepository.SaveChangesAsync();
        }

        public async Task<User?> GetUserByEmailAsync(string email)
        {
            return await _userRepository.GetUserByEmailAsync(email);
        }

        public async Task<VolunteerProfileDto?> GetVolunteerProfileAsync(int userId)
        {
            var user = await _context.Users
                .Where(u => u.UserId == userId)
                .Include(u => u.VolunteerSkills)
                    .ThenInclude(vs => vs.Skill)
                .Include(u => u.VolunteerCauses)
                    .ThenInclude(vc => vc.Cause)
                .FirstOrDefaultAsync();

            if (user == null) return null;

            var profileDto = new VolunteerProfileDto
            {
                UserId = user.UserId,
                FullName = user.FullName,
                Email = user.Email,
                PhoneNumber = user.PhoneNumber,
                DateOfBirth = user.DateOfBirth,
                District = user.District,
                Skills = user.VolunteerSkills.Select(vs => vs.Skill.SkillName).ToList(),
                Causes = user.VolunteerCauses.Select(vc => vc.Cause.CauseName).ToList()
            };

            return profileDto;
        }

        // Xử lý logic đăng ký người dùng mới
        public async Task<(User? user, string? errorMessage)> RegisterUserAsync(RegisterRequestDto registerDto)
        {
            // Kiểm tra email đã tồn tại chưa
            var existingUser = await _userRepository.GetUserByEmailAsync(registerDto.Email);
            if (existingUser != null)
            {
                return (null, "Email đã được sử dụng.");
            }

            // Tìm RoleId dựa trên chuỗi Role gửi lên
            var role = await _roleRepository.GetRoleByNameAsync(registerDto.Role);
            if (role == null)
            {
                return (null, "Vai trò không hợp lệ.");
            }

            // Băm mật khẩu (trong thực tế dùng thư viện như BCrypt.Net)
            var hashedPassword = registerDto.Password; // Tạm thời không băm để dễ test

            var user = new User
            {
                Email = registerDto.Email,
                PasswordHash = hashedPassword,
                FullName = registerDto.FullName,
            };

            // Dùng transaction để đảm bảo tính toàn vẹn dữ liệu
            using var transaction = await _context.Database.BeginTransactionAsync();
            try
            {
                // 1. Tạo User
                await _userRepository.AddAsync(user);
                await _userRepository.SaveChangesAsync();

                // 2. Gán Role cho User
                var userRole = new UserRole { UserId = user.UserId, RoleId = role.RoleId };
                _context.UserRoles.Add(userRole);
                await _context.SaveChangesAsync();

                // 3. Nếu là Organization, tạo bản ghi Organization
                if (role.RoleName.Equals("Organization", System.StringComparison.OrdinalIgnoreCase))
                {
                    if (string.IsNullOrWhiteSpace(registerDto.OrganizationName))
                    {
                        await transaction.RollbackAsync();
                        return (null, "Tên tổ chức là bắt buộc.");
                    }
                    var organization = new Organization
                    {
                        UserId = user.UserId,
                        OrganizationName = registerDto.OrganizationName,
                        ContactPerson = user.FullName // Mặc định người liên hệ là người tạo tài khoản
                    };
                    await _organizationRepository.AddAsync(organization);
                    await _organizationRepository.SaveChangesAsync();
                }

                // 4. Tạo thông báo chào mừng
                var message = "Chào mừng bạn đến với HanoConnect! Hãy bắt đầu hành trình kết nối và cống hiến ngay hôm nay.";
                await _notificationService.CreateNotificationAsync(user.UserId, message);

                // Hoàn tất transaction
                await transaction.CommitAsync();

                return (user, null);
            }
            catch (System.Exception ex)
            {
                await transaction.RollbackAsync();
                // Log lỗi ra console của server để debug
                System.Console.WriteLine(ex.ToString());
                return (null, "Đã có lỗi xảy ra trong quá trình đăng ký.");
            }
        }

        public async Task<(bool success, string? errorMessage)> UpdateVolunteerProfileAsync(int userId, VolunteerProfileUpdateDto updateDto)
        {
            var user = await _context.Users
                .Include(u => u.VolunteerSkills)
                .Include(u => u.VolunteerCauses)
                .FirstOrDefaultAsync(u => u.UserId == userId);

            if (user == null)
            {
                return (false, "Không tìm thấy người dùng.");
            }

            using var transaction = await _context.Database.BeginTransactionAsync();
            try
            {
                user.FullName = updateDto.FullName;
                user.PhoneNumber = updateDto.PhoneNumber;
                user.District = updateDto.District;
                user.UpdatedAt = System.DateTime.UtcNow;
                _userRepository.Update(user);

                var currentSkillIds = user.VolunteerSkills.Select(s => s.SkillId).ToList();
                var skillsToRemove = user.VolunteerSkills.Where(s => !updateDto.SkillIds.Contains(s.SkillId)).ToList();
                var skillIdsToAdd = updateDto.SkillIds.Where(id => !currentSkillIds.Contains(id)).ToList();

                _context.VolunteerSkills.RemoveRange(skillsToRemove);
                foreach (var skillId in skillIdsToAdd)
                {
                    _context.VolunteerSkills.Add(new VolunteerSkill { UserId = userId, SkillId = skillId });
                }

                var currentCauseIds = user.VolunteerCauses.Select(c => c.CauseId).ToList();
                var causesToRemove = user.VolunteerCauses.Where(c => !updateDto.CauseIds.Contains(c.CauseId)).ToList();
                var causeIdsToAdd = updateDto.CauseIds.Where(id => !currentCauseIds.Contains(id)).ToList();

                _context.VolunteerCauses.RemoveRange(causesToRemove);
                foreach (var causeId in causeIdsToAdd)
                {
                    _context.VolunteerCauses.Add(new VolunteerCause { UserId = userId, CauseId = causeId });
                }

                await _context.SaveChangesAsync();
                await transaction.CommitAsync();

                return (true, null);
            }
            catch (System.Exception ex)
            {
                await transaction.RollbackAsync();
                System.Console.WriteLine(ex);
                return (false, "Đã có lỗi xảy ra khi cập nhật.");
            }
        }
    }
}
