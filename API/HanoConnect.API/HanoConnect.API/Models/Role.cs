using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema; // Added for Table/Column attributes if needed, good practice

namespace HanoConnect.API.Models
{
    [Table("Roles")] // Explicitly map to "Roles" table
    public class Role
    {
        [Key] // Denotes primary key
        [DatabaseGenerated(DatabaseGeneratedOption.Identity)] // Auto-incrementing identity column
        public int RoleId { get; set; }

        [Required] // NOT NULL
        [MaxLength(50)] // NVARCHAR(50)
        public required string RoleName { get; set; } // Đã thêm từ khóa 'required'

        // Navigation property for one-to-many relationship with UserRoles
        public ICollection<UserRole> UserRoles { get; set; } = new List<UserRole>();
    }
}