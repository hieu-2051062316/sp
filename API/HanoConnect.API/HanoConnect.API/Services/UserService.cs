using HanoConnect.API.Interfaces;
using HanoConnect.API.Models;
using System.Collections.Generic;
using System.Threading.Tasks;

namespace HanoConnect.API.Services
{
    public class UserService : IUserService
    {
        private readonly IUserRepository _userRepository;
<<<<<<< Updated upstream

        public UserService(IUserRepository userRepository)
        {
            _userRepository = userRepository;
=======
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
>>>>>>> Stashed changes
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
            return user; // Trả về user đã được thêm (có thể đã có Id sau khi lưu)
        }

        public async Task<bool> UpdateUserAsync(User user)
        {
            var existingUser = await _userRepository.GetByIdAsync(user.UserId);
            if (existingUser == null)
            {
                return false;
            }

<<<<<<< Updated upstream
            // Cập nhật các thuộc tính cần thiết từ user mới vào existingUser
=======
>>>>>>> Stashed changes
            existingUser.Email = user.Email;
            existingUser.PasswordHash = user.PasswordHash; // Lưu ý: cần xử lý băm mật khẩu
            existingUser.FullName = user.FullName;
            existingUser.PhoneNumber = user.PhoneNumber;
            existingUser.DateOfBirth = user.DateOfBirth;
            existingUser.District = user.District;
            // UpdatedAt sẽ được trigger tự động bởi database

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
<<<<<<< Updated upstream
=======

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
                        ContactPerson = user.FullName
                    };
                    await _organizationRepository.AddAsync(organization);
                    await _organizationRepository.SaveChangesAsync();
                }

                var message = "Chào mừng bạn đến với HanoConnect! Hãy bắt đầu hành trình kết nối và cống hiến ngay hôm nay.";
                await _notificationService.CreateNotificationAsync(user.UserId, message);

                await transaction.CommitAsync();

                return (user, null);
            }
            catch (System.Exception ex)
            {
                await transaction.RollbackAsync();
                System.Console.WriteLine(ex.ToString());
                return (null, "Đã có lỗi xảy ra trong quá trình đăng ký.");
            }
        }
>>>>>>> Stashed changes
    }
}