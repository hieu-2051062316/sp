using HanoConnect.API.Data; // Thêm dòng này để sử dụng ApplicationDbContext
using Microsoft.EntityFrameworkCore; // Thêm dòng này để sử dụng các phương thức mở rộng của EF Core

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