USE HanoConnectDB;
GO

-- 1. Thêm User mới
INSERT INTO Users (Email, PasswordHash, FullName, PhoneNumber, DateOfBirth, District)
VALUES ('new.volunteer@example.com', 'hashed_password_new', N'Tình nguyện viên Mới', '0912345678', '1999-12-12', N'Tây Hồ');
GO

-- 2. Gán vai trò 'Volunteer' cho User vừa tạo
-- Giả định rằng Role 'Volunteer' có RoleId = 2
INSERT INTO UserRoles (UserId, RoleId)
VALUES (
    (SELECT UserId FROM Users WHERE Email = 'new.volunteer@example.com'), 
    (SELECT RoleId FROM Roles WHERE RoleName = 'Volunteer')
);
GO

