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
        private readonly ApplicationDbContext _context;

        public UserService(IUserRepository userRepository, ApplicationDbContext context)
        {
            _userRepository = userRepository;
            _context = context;
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
            // Trong thực tế, cần xử lý băm mật khẩu ở đây
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

            // Cập nhật các thuộc tính
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
    }
}
