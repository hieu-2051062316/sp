using HanoConnect.API.Interfaces;
using HanoConnect.API.Models;
using System.Collections.Generic;
using System.Threading.Tasks;

namespace HanoConnect.API.Services
{
    public class CauseService : ICauseService
    {
        private readonly ICauseRepository _causeRepository;

        public CauseService(ICauseRepository causeRepository)
        {
            _causeRepository = causeRepository;
        }

        public async Task<IEnumerable<Cause>> GetAllCausesAsync()
        {
            return await _causeRepository.GetAllAsync();
        }

        public async Task<Cause?> GetCauseByIdAsync(int id)
        {
            return await _causeRepository.GetByIdAsync(id);
        }

        public async Task<Cause?> AddCauseAsync(Cause cause)
        {
            await _causeRepository.AddAsync(cause);
            await _causeRepository.SaveChangesAsync();
            return cause;
        }

        public async Task<bool> UpdateCauseAsync(Cause cause)
        {
            var existingCause = await _causeRepository.GetByIdAsync(cause.CauseId);
            if (existingCause == null)
            {
                return false;
            }

            existingCause.CauseName = cause.CauseName;
            // Add any other properties that can be updated

            _causeRepository.Update(existingCause);
            return await _causeRepository.SaveChangesAsync();
        }

        public async Task<bool> DeleteCauseAsync(int id)
        {
            var causeToDelete = await _causeRepository.GetByIdAsync(id);
            if (causeToDelete == null)
            {
                return false;
            }
            _causeRepository.Delete(causeToDelete);
            return await _causeRepository.SaveChangesAsync();
        }

        public async Task<Cause?> GetCauseByNameAsync(string causeName)
        {
            return await _causeRepository.GetCauseByNameAsync(causeName);
        }
    }
}