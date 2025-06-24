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
    public class UserService : IUserService
    {
        private readonly IUserRepository _userRepository;
        private readonly IRoleRepository _roleRepository;
        private readonly IOrganizationRepository _organizationRepository;
        private readonly ApplicationDbContext _context;

        // Constructor đã được dọn dẹp, không còn chứa các interface không cần thiết
        public UserService(
            IUserRepository userRepository,
            ApplicationDbContext context,
            IRoleRepository roleRepository,
            IOrganizationRepository organizationRepository)
        {
            _userRepository = userRepository;
            _context = context;
            _roleRepository = roleRepository;
            _organizationRepository = organizationRepository;
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
            existingUser.UpdatedAt = DateTime.UtcNow;

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

        // Lấy thông tin chi tiết cho trang Profile của Volunteer
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

        // Logic đăng ký không còn tạo thông báo
        public async Task<(User? user, string? errorMessage)> RegisterUserAsync(RegisterRequestDto registerDto)
        {
            var existingUser = await _userRepository.GetUserByEmailAsync(registerDto.Email);
            if (existingUser != null)
            {
                return (null, "Email đã được sử dụng.");
            }

            var role = await _roleRepository.GetRoleByNameAsync(registerDto.Role);
            if (role == null)
            {
                return (null, "Vai trò không hợp lệ.");
            }

            // Băm mật khẩu bằng BCrypt
            var hashedPassword = BCrypt.Net.BCrypt.HashPassword(registerDto.Password);

            var user = new User
            {
                Email = registerDto.Email,
                PasswordHash = hashedPassword,
                FullName = registerDto.FullName,
                PhoneNumber = registerDto.PhoneNumber,
                District = registerDto.District
            };

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
                if (role.RoleName.Equals("Organization", StringComparison.OrdinalIgnoreCase))
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
                        ContactPerson = user.FullName,
                        Address = registerDto.Address,
                        Website = registerDto.Website,
                        Description = registerDto.Description
                    };
                    await _organizationRepository.AddAsync(organization);
                    await _organizationRepository.SaveChangesAsync();
                }

                await transaction.CommitAsync();
                return (user, null);
            }
            catch (Exception ex)
            {
                await transaction.RollbackAsync();
                Console.WriteLine(ex.ToString());
                return (null, "Đã có lỗi xảy ra trong quá trình đăng ký.");
            }
        }

        // Xử lý logic cập nhật profile
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

            // Cập nhật thông tin cơ bản
            user.FullName = updateDto.FullName;
            user.PhoneNumber = updateDto.PhoneNumber;
            user.District = updateDto.District;
            user.UpdatedAt = DateTime.UtcNow;

            // Xóa hết các kỹ năng và lĩnh vực cũ để thêm lại
            _context.VolunteerSkills.RemoveRange(user.VolunteerSkills);
            _context.VolunteerCauses.RemoveRange(user.VolunteerCauses);

            // Thêm lại các kỹ năng mới từ danh sách ID
            if (updateDto.SkillIds != null)
            {
                foreach (var skillId in updateDto.SkillIds)
                {
                    _context.VolunteerSkills.Add(new VolunteerSkill { UserId = userId, SkillId = skillId });
                }
            }

            // Thêm lại các lĩnh vực mới từ danh sách ID
            if (updateDto.CauseIds != null)
            {
                foreach (var causeId in updateDto.CauseIds)
                {
                    _context.VolunteerCauses.Add(new VolunteerCause { UserId = userId, CauseId = causeId });
                }
            }

            try
            {
                await _context.SaveChangesAsync();
                return (true, null);
            }
            catch (Exception ex)
            {
                Console.WriteLine(ex);
                return (false, "Đã có lỗi xảy ra khi lưu vào cơ sở dữ liệu.");
            }
        }
    }
}
