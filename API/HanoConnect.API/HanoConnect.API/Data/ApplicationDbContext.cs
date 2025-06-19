using Microsoft.EntityFrameworkCore;
using HanoConnect.API.Models; // Đảm bảo đã include namespace của Models

namespace HanoConnect.API.Data
{
    public class ApplicationDbContext : DbContext
    {
        public ApplicationDbContext(DbContextOptions<ApplicationDbContext> options)
            : base(options)
        {
        }

        // Define DbSet for each table in your database
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

            // Configure unique constraint for RoleName
            modelBuilder.Entity<Role>()
                .HasIndex(r => r.RoleName)
                .IsUnique();

            // Configure unique constraint for Email in Users
            modelBuilder.Entity<User>()
                .HasIndex(u => u.Email)
                .IsUnique();

            // Configure UNIQUE constraint for UserId, RoleId in UserRoles
            modelBuilder.Entity<UserRole>()
                .HasIndex(ur => new { ur.UserId, ur.RoleId })
                .IsUnique();

            // Configure UNIQUE constraint for UserId in Organizations (UserId is unique FK)
            modelBuilder.Entity<Organization>()
                .HasIndex(o => o.UserId)
                .IsUnique();

            // Configure unique constraint for SkillName
            modelBuilder.Entity<Skill>()
                .HasIndex(s => s.SkillName)
                .IsUnique();

            // Configure unique constraint for CauseName
            modelBuilder.Entity<Cause>()
                .HasIndex(c => c.CauseName)
                .IsUnique();

            // Configure UNIQUE constraint for UserId, SkillId in VolunteerSkills
            modelBuilder.Entity<VolunteerSkill>()
                .HasIndex(vs => new { vs.UserId, vs.SkillId })
                .IsUnique();

            // Configure UNIQUE constraint for UserId, CauseId in VolunteerCauses
            modelBuilder.Entity<VolunteerCause>()
                .HasIndex(vc => new { vc.UserId, vc.CauseId })
                .IsUnique();

            // Configure UNIQUE constraint for OpportunityId, SkillId in OpportunitySkills
            modelBuilder.Entity<OpportunitySkill>()
                .HasIndex(os => new { os.OpportunityId, os.SkillId })
                .IsUnique();


            // --- Define Relationships ---

            // UserRoles (Many-to-Many between User and Role via UserRole table)
            modelBuilder.Entity<UserRole>()
                .HasOne(ur => ur.User)
                .WithMany(u => u.UserRoles)
                .HasForeignKey(ur => ur.UserId);

            modelBuilder.Entity<UserRole>()
                .HasOne(ur => ur.Role)
                .WithMany(r => r.UserRoles)
                .HasForeignKey(ur => ur.RoleId);

            // Organization (One-to-One between User and Organization)
            // A user account can be linked to at most one organization, and an organization has exactly one user account.
            modelBuilder.Entity<User>()
                .HasOne(u => u.Organization) // User has one Organization
                .WithOne(o => o.User)         // Organization has one User
                .HasForeignKey<Organization>(o => o.UserId); // FK is in Organization table

            // Organization verification by Admin User (One-to-Many from User to Organization)
            // An admin user can verify multiple organizations. VerifiedByAdminId is nullable.
            modelBuilder.Entity<Organization>()
                .HasOne(o => o.VerifiedByAdmin)
                .WithMany(u => u.VerifiedOrganizations)
                .HasForeignKey(o => o.VerifiedByAdminId)
                .IsRequired(false); // VerifiedByAdminId is NULLABLE

            // VolunteerSkills (Many-to-Many between User and Skill via VolunteerSkill table)
            modelBuilder.Entity<VolunteerSkill>()
                .HasOne(vs => vs.User)
                .WithMany(u => u.VolunteerSkills)
                .HasForeignKey(vs => vs.UserId);

            modelBuilder.Entity<VolunteerSkill>()
                .HasOne(vs => vs.Skill)
                .WithMany(s => s.VolunteerSkills)
                .HasForeignKey(vs => vs.SkillId);

            // VolunteerCauses (Many-to-Many between User and Cause via VolunteerCause table)
            modelBuilder.Entity<VolunteerCause>()
                .HasOne(vc => vc.User)
                .WithMany(u => u.VolunteerCauses)
                .HasForeignKey(vc => vc.UserId);

            modelBuilder.Entity<VolunteerCause>()
                .HasOne(vc => vc.Cause)
                .WithMany(c => c.VolunteerCauses)
                .HasForeignKey(vc => vc.CauseId);

            // Opportunities (One-to-Many from Organization to Opportunity)
            modelBuilder.Entity<Opportunity>()
                .HasOne(o => o.Organization)
                .WithMany(org => org.Opportunities)
                .HasForeignKey(o => o.OrganizationId);

            // Opportunities (One-to-Many from Cause to Opportunity)
            modelBuilder.Entity<Opportunity>()
                .HasOne(o => o.Cause)
                .WithMany(c => c.Opportunities)
                .HasForeignKey(o => o.CauseId);

            // OpportunitySkills (Many-to-Many between Opportunity and Skill via OpportunitySkill table)
            modelBuilder.Entity<OpportunitySkill>()
                .HasOne(os => os.Opportunity)
                .WithMany(o => o.OpportunitySkills)
                .HasForeignKey(os => os.OpportunityId);

            modelBuilder.Entity<OpportunitySkill>()
                .HasOne(os => os.Skill)
                .WithMany(s => s.OpportunitySkills)
                .HasForeignKey(os => os.SkillId);

            // Applications (One-to-Many from Opportunity to Application)
            modelBuilder.Entity<Application>()
                .HasOne(a => a.Opportunity)
                .WithMany(o => o.Applications)
                .HasForeignKey(a => a.OpportunityId);

            // Applications (One-to-Many from User to Application - for VolunteerUserId)
            modelBuilder.Entity<Application>()
                .HasOne(a => a.VolunteerUser)
                .WithMany(u => u.Applications)
                .HasForeignKey(a => a.VolunteerUserId);

            // Feedback relationships
            // Feedback to Application (Nullable One-to-Many from Application to Feedback if one application can have many feedbacks, or One-to-One)
            // Here, I am modeling it as one Feedback can optionally relate to one Application.
            modelBuilder.Entity<Feedback>()
                .HasOne(f => f.Application)
                .WithMany() // No navigation property on Application for Feedback. If you want, add ICollection<Feedback> to Application model.
                .HasForeignKey(f => f.ApplicationId)
                .IsRequired(false); // ApplicationId is NULLABLE in your DB schema

            // Feedback from RaterUser (User giving feedback)
            modelBuilder.Entity<Feedback>()
                .HasOne(f => f.RaterUser)
                .WithMany(u => u.GivenFeedbacks) // Using GivenFeedbacks on User model
                .HasForeignKey(f => f.RaterUserId)
                .OnDelete(DeleteBehavior.NoAction); // Prevent cyclic cascade delete

            // Feedback to RatedUser (User receiving feedback)
            modelBuilder.Entity<Feedback>()
                .HasOne(f => f.RatedUser)
                .WithMany(u => u.ReceivedFeedbacksAsRatedUser) // Using ReceivedFeedbacksAsRatedUser on User model
                .HasForeignKey(f => f.RatedUserId)
                .IsRequired(false) // RatedUserId is NULLABLE
                .OnDelete(DeleteBehavior.NoAction); // Prevent cyclic cascade delete

            // Feedback to RatedOrganization (Organization receiving feedback)
            modelBuilder.Entity<Feedback>()
                .HasOne(f => f.RatedOrganization)
                .WithMany(o => o.ReceivedFeedbacks) // Using ReceivedFeedbacks on Organization model
                .HasForeignKey(f => f.RatedOrganizationId)
                .IsRequired(false) // RatedOrganizationId is NULLABLE
                .OnDelete(DeleteBehavior.NoAction); // Prevent cyclic cascade delete

            // Set default values for DATETIME2 columns that have DEFAULT GETDATE() in SQL
            // EF Core will handle this automatically when adding new entities if the property is not set,
            // but explicitly setting it in the model or during creation is good practice.
            // For existing data, you'd use migrations or raw SQL.
            modelBuilder.Entity<User>()
                .Property(u => u.CreatedAt)
                .HasDefaultValueSql("GETDATE()");
            modelBuilder.Entity<User>()
                .Property(u => u.UpdatedAt)
                .HasDefaultValueSql("GETDATE()");

            modelBuilder.Entity<Organization>()
                .Property(o => o.CreatedAt)
                .HasDefaultValueSql("GETDATE()");
            modelBuilder.Entity<Organization>()
                .Property(o => o.UpdatedAt)
                .HasDefaultValueSql("GETDATE()");

            modelBuilder.Entity<Opportunity>()
                .Property(o => o.CreatedAt)
                .HasDefaultValueSql("GETDATE()");
            modelBuilder.Entity<Opportunity>()
                .Property(o => o.UpdatedAt)
                .HasDefaultValueSql("GETDATE()");

            modelBuilder.Entity<Application>()
                .Property(a => a.ApplicationTime)
                .HasDefaultValueSql("GETDATE()");

            modelBuilder.Entity<Feedback>()
                .Property(f => f.FeedbackTime)
                .HasDefaultValueSql("GETDATE()");
        }
    }
}