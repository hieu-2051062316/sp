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

        // Xử lý logic đăng ký người dùng mới
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

            var hashedPassword = registerDto.Password;

            var user = new User
            {
                Email = registerDto.Email,
                PasswordHash = hashedPassword,
                FullName = registerDto.FullName,
            };

            using var transaction = await _context.Database.BeginTransactionAsync();
            try
            {
                await _userRepository.AddAsync(user);
                await _userRepository.SaveChangesAsync();

                var userRole = new UserRole { UserId = user.UserId, RoleId = role.RoleId };
                _context.UserRoles.Add(userRole);
                await _context.SaveChangesAsync();

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
                        ContactPerson = user.FullName
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

            using var transaction = await _context.Database.BeginTransactionAsync();
            try
            {
                // Cập nhật thông tin cơ bản
                user.FullName = updateDto.FullName;
                user.PhoneNumber = updateDto.PhoneNumber;
                user.District = updateDto.District;
                user.UpdatedAt = DateTime.UtcNow;
                _userRepository.Update(user);

                // Xử lý cập nhật Kỹ năng (Skills)
                var currentSkillIds = user.VolunteerSkills.Select(s => s.SkillId).ToList();
                var skillsToRemove = user.VolunteerSkills.Where(s => !updateDto.SkillIds.Contains(s.SkillId)).ToList();
                var skillIdsToAdd = updateDto.SkillIds.Where(id => !currentSkillIds.Contains(id)).ToList();

                _context.VolunteerSkills.RemoveRange(skillsToRemove);
                foreach (var skillId in skillIdsToAdd)
                {
                    _context.VolunteerSkills.Add(new VolunteerSkill { UserId = userId, SkillId = skillId });
                }

                // Xử lý cập nhật Lĩnh vực (Causes)
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
            catch (Exception ex)
            {
                await transaction.RollbackAsync();
                Console.WriteLine(ex);
                return (false, "Đã có lỗi xảy ra khi cập nhật.");
            }
        }
    }
}
