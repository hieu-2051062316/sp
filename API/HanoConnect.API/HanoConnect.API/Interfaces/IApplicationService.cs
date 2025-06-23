using HanoConnect.API.DTOs;
using HanoConnect.API.Models;
using System.Threading.Tasks;

namespace HanoConnect.API.Interfaces
{
    public interface IApplicationService
    {
        // Trả về một tuple chứa application hoặc một thông báo lỗi
        Task<(Application? application, string? errorMessage)> CreateApplicationAsync(ApplyDto applyDto);
    }
}
