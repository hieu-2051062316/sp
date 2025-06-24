<<<<<<< Updated upstream
﻿using HanoConnect.API.Models;
=======
﻿using HanoConnect.API.DTOs;
using HanoConnect.API.Models;
>>>>>>> Stashed changes
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
<<<<<<< Updated upstream
        Task<User?> GetUserByEmailAsync(string email); // Thêm phương thức này
=======
        Task<User?> GetUserByEmailAsync(string email);
        Task<VolunteerProfileDto?> GetVolunteerProfileAsync(int userId);

        // Xử lý logic đăng ký người dùng mới
        Task<(User? user, string? errorMessage)> RegisterUserAsync(RegisterRequestDto registerDto);
>>>>>>> Stashed changes
    }
}