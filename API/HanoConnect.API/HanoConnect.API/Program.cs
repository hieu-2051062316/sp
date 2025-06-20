using HanoConnect.API.Data;
using HanoConnect.API.Interfaces; // Thêm dòng này để sử dụng IGenericRepository
using HanoConnect.API.Repositories; // Thêm dòng này để sử dụng GenericRepository
using HanoConnect.API.Services;
using Microsoft.EntityFrameworkCore;

var builder = WebApplication.CreateBuilder(args);

// Add services to the container.

builder.Services.AddControllers();
// Learn more about configuring Swagger/OpenAPI at https://aka.ms/aspnetcore/swashbuckle
builder.Services.AddEndpointsApiExplorer();
builder.Services.AddSwaggerGen();

// --- Bắt đầu phần thêm code của bạn ---

// Đăng ký ApplicationDbContext với SQL Server và chuỗi kết nối từ appsettings.Development.json
builder.Services.AddDbContext<ApplicationDbContext>(options =>
    options.UseSqlServer(builder.Configuration.GetConnectionString("DefaultConnection")));

// Register Generic Repository
builder.Services.AddScoped(typeof(IGenericRepository<>), typeof(GenericRepository<>));

// Register specific Repositories
builder.Services.AddScoped<IUserRepository, UserRepository>();
builder.Services.AddScoped<IRoleRepository, RoleRepository>();
builder.Services.AddScoped<ICauseRepository, CauseRepository>();
builder.Services.AddScoped<ISkillRepository, SkillRepository>();
builder.Services.AddScoped<IOrganizationRepository, OrganizationRepository>();

// Register Services
builder.Services.AddScoped<IUserService, UserService>();
builder.Services.AddScoped<IRoleService, RoleService>();
builder.Services.AddScoped<ICauseService, CauseService>();
builder.Services.AddScoped<ISkillService, SkillService>();
builder.Services.AddScoped<IOrganizationService, OrganizationService>();

// --- Kết thúc phần thêm code của bạn ---

var app = builder.Build();

// Configure the HTTP request pipeline.
if (app.Environment.IsDevelopment())
{
    app.UseSwagger();
    app.UseSwaggerUI();
}

app.UseHttpsRedirection();

app.UseAuthorization();

app.MapControllers();

app.Run();