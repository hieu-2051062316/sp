using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace HanoConnect.API.Models
{
    [Table("UserRoles")]
    public class UserRole
    {
        [Key] // Primary key for this junction table
        [DatabaseGenerated(DatabaseGeneratedOption.Identity)] // Auto-incrementing identity column
        public int UserRoleId { get; set; } // SQL Server uses identity column, so we need a single PK here

        public int UserId { get; set; }
        public int RoleId { get; set; }

        // Navigation properties for foreign keys
        [ForeignKey("UserId")]
        public required User User { get; set; } // Added 'required'

        [ForeignKey("RoleId")]
        public required Role Role { get; set; } // Added 'required'
    }
}