using HanoConnect.API.DTOs; // Cần cho DTO
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

        // Lấy thông tin profile của một Volunteer
        Task<VolunteerProfileDto?> GetVolunteerProfileAsync(int userId);
    }
}
