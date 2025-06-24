using HanoConnect.API.DTOs;
using HanoConnect.API.Models;
using System.Collections.Generic;
using System.Threading.Tasks;

namespace HanoConnect.API.Interfaces
{
    public interface IUserService
    {
        Task<IEnumerable<User>> GetAllUsersAsync();
        Task<User?> GetUserByIdAsync(int id);
        Task<User?> AddUserAsync(User user);
        Task<bool> UpdateUserAsync(User user);
        Task<bool> DeleteUserAsync(int id);
        Task<User?> GetUserByEmailAsync(string email);
        Task<VolunteerProfileDto?> GetVolunteerProfileAsync(int userId);
        Task<(User? user, string? errorMessage)> RegisterUserAsync(RegisterRequestDto registerDto);

        // Xử lý logic cập nhật profile
        Task<(bool success, string? errorMessage)> UpdateVolunteerProfileAsync(int userId, VolunteerProfileUpdateDto updateDto);
    }
}
