using Microsoft.EntityFrameworkCore;
using HanoConnect.API.Models;

namespace HanoConnect.API.Data
{
    public class ApplicationDbContext : DbContext
    {
        public ApplicationDbContext(DbContextOptions<ApplicationDbContext> options)
            : base(options)
        {
        }

        // Khai báo các DbSet
        public DbSet<Role> Roles { get; set; }
        public DbSet<User> Users { get; set; }
        public DbSet<UserRole> UserRoles { get; set; }
        public DbSet<Organization> Organizations { get; set; }
        public DbSet<Skill> Skills { get; set; }
        public DbSet<Cause> Causes { get; set; }
        public DbSet<VolunteerSkill> VolunteerSkills { get; set; }
        public DbSet<VolunteerCause> VolunteerCauses { get; set; }
        public DbSet<Opportunity> Opportunities { get; set; }
        public DbSet<OpportunitySkill> OpportunitySkills { get; set; }
        public DbSet<Application> Applications { get; set; }
        public DbSet<Feedback> Feedbacks { get; set; }

        protected override void OnModelCreating(ModelBuilder modelBuilder)
        {
            base.OnModelCreating(modelBuilder);

            // Cấu hình các ràng buộc UNIQUE
            modelBuilder.Entity<Role>().HasIndex(r => r.RoleName).IsUnique();
            modelBuilder.Entity<User>().HasIndex(u => u.Email).IsUnique();
            modelBuilder.Entity<Organization>().HasIndex(o => o.UserId).IsUnique();
            modelBuilder.Entity<Skill>().HasIndex(s => s.SkillName).IsUnique();
            modelBuilder.Entity<Cause>().HasIndex(c => c.CauseName).IsUnique();

            // Cấu hình các khóa phức hợp UNIQUE
            modelBuilder.Entity<UserRole>().HasIndex(ur => new { ur.UserId, ur.RoleId }).IsUnique();
            modelBuilder.Entity<VolunteerSkill>().HasIndex(vs => new { vs.UserId, vs.SkillId }).IsUnique();
            modelBuilder.Entity<VolunteerCause>().HasIndex(vc => new { vc.UserId, vc.CauseId }).IsUnique();
            modelBuilder.Entity<OpportunitySkill>().HasIndex(os => new { os.OpportunityId, os.SkillId }).IsUnique();

            // Cấu hình các mối quan hệ (Relationships)

            // User-Role (many-to-many)
            modelBuilder.Entity<UserRole>()
                .HasOne(ur => ur.User).WithMany(u => u.UserRoles).HasForeignKey(ur => ur.UserId);
            modelBuilder.Entity<UserRole>()
                .HasOne(ur => ur.Role).WithMany(r => r.UserRoles).HasForeignKey(ur => ur.RoleId);

            // User-Organization (one-to-one)
            modelBuilder.Entity<User>()
                .HasOne(u => u.Organization).WithOne(o => o.User).HasForeignKey<Organization>(o => o.UserId);

            // Admin-Organization (one-to-many)
            modelBuilder.Entity<Organization>()
                .HasOne(o => o.VerifiedByAdmin)
                .WithMany(u => u.VerifiedOrganizations)
                .HasForeignKey(o => o.VerifiedByAdminId)
                .IsRequired(false);

            // Volunteer-Skill (many-to-many)
            modelBuilder.Entity<VolunteerSkill>()
                .HasOne(vs => vs.User).WithMany(u => u.VolunteerSkills).HasForeignKey(vs => vs.UserId);
            modelBuilder.Entity<VolunteerSkill>()
                .HasOne(vs => vs.Skill).WithMany(s => s.VolunteerSkills).HasForeignKey(vs => vs.SkillId);

            // Volunteer-Cause (many-to-many)
            modelBuilder.Entity<VolunteerCause>()
                .HasOne(vc => vc.User).WithMany(u => u.VolunteerCauses).HasForeignKey(vc => vc.UserId);
            modelBuilder.Entity<VolunteerCause>()
                .HasOne(vc => vc.Cause).WithMany(c => c.VolunteerCauses).HasForeignKey(vc => vc.CauseId);

            // Opportunity-Skill (many-to-many)
            modelBuilder.Entity<OpportunitySkill>()
                .HasOne(os => os.Opportunity).WithMany(o => o.OpportunitySkills).HasForeignKey(os => os.OpportunityId);
            modelBuilder.Entity<OpportunitySkill>()
                .HasOne(os => os.Skill).WithMany(s => s.OpportunitySkills).HasForeignKey(os => os.SkillId);

            // Application-User & Application-Opportunity
            modelBuilder.Entity<Application>()
                .HasOne(a => a.Opportunity).WithMany(o => o.Applications).HasForeignKey(a => a.OpportunityId);
            modelBuilder.Entity<Application>()
                .HasOne(a => a.VolunteerUser).WithMany(u => u.Applications).HasForeignKey(a => a.VolunteerUserId);

            // Cấu hình các mối quan hệ cho Feedback (với NoAction để tránh lỗi vòng lặp)
            modelBuilder.Entity<Feedback>()
                .HasOne(f => f.RaterUser).WithMany(u => u.GivenFeedbacks).HasForeignKey(f => f.RaterUserId).OnDelete(DeleteBehavior.NoAction);
            modelBuilder.Entity<Feedback>()
                .HasOne(f => f.RatedUser).WithMany(u => u.ReceivedFeedbacksAsRatedUser).HasForeignKey(f => f.RatedUserId).IsRequired(false).OnDelete(DeleteBehavior.NoAction);
            modelBuilder.Entity<Feedback>()
                .HasOne(f => f.RatedOrganization).WithMany(o => o.ReceivedFeedbacks).HasForeignKey(f => f.RatedOrganizationId).IsRequired(false).OnDelete(DeleteBehavior.NoAction);

            // Cấu hình giá trị mặc định cho các cột ngày tháng
            modelBuilder.Entity<User>().Property(u => u.CreatedAt).HasDefaultValueSql("GETDATE()");
            modelBuilder.Entity<User>().Property(u => u.UpdatedAt).HasDefaultValueSql("GETDATE()");
            modelBuilder.Entity<Organization>().Property(o => o.CreatedAt).HasDefaultValueSql("GETDATE()");
            modelBuilder.Entity<Organization>().Property(o => o.UpdatedAt).HasDefaultValueSql("GETDATE()");
            modelBuilder.Entity<Opportunity>().Property(o => o.CreatedAt).HasDefaultValueSql("GETDATE()");
            modelBuilder.Entity<Opportunity>().Property(o => o.UpdatedAt).HasDefaultValueSql("GETDATE()");
            modelBuilder.Entity<Application>().Property(a => a.ApplicationTime).HasDefaultValueSql("GETDATE()");
            modelBuilder.Entity<Feedback>().Property(f => f.FeedbackTime).HasDefaultValueSql("GETDATE()");
        }
    }
}
