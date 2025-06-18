-- Đảm bảo đang sử dụng đúng database
USE HanoConnectDB;
GO

-- Bảng Roles
CREATE TABLE Roles (
    RoleId INT PRIMARY KEY IDENTITY(1,1),
    RoleName NVARCHAR(50) NOT NULL UNIQUE
);
GO

-- Bảng Users
CREATE TABLE Users (
    UserId INT PRIMARY KEY IDENTITY(1,1),
    Email NVARCHAR(255) NOT NULL UNIQUE,
    PasswordHash NVARCHAR(255) NOT NULL, -- Sẽ lưu trữ mật khẩu đã băm
    FullName NVARCHAR(255) NULL,
    PhoneNumber NVARCHAR(20) NULL, -- Sẽ xác thực OTP sau
    DateOfBirth DATE NULL,
    District NVARCHAR(100) NULL, -- Quận/Huyện cư trú
    CreatedAt DATETIME2 DEFAULT GETDATE(),
    UpdatedAt DATETIME2 DEFAULT GETDATE()
);
GO

-- Bảng UserRoles
CREATE TABLE UserRoles (
    UserRoleId INT PRIMARY KEY IDENTITY(1,1),
    UserId INT NOT NULL,
    RoleId INT NOT NULL,
    FOREIGN KEY (UserId) REFERENCES Users(UserId),
    FOREIGN KEY (RoleId) REFERENCES Roles(RoleId),
    UNIQUE (UserId, RoleId) -- Đảm bảo một người dùng chỉ có một vai trò duy nhất trong bảng này
);
GO

-- Bảng Organizations
CREATE TABLE Organizations (
    OrganizationId INT PRIMARY KEY IDENTITY(1,1),
    UserId INT UNIQUE NOT NULL, -- Liên kết với tài khoản người dùng
    OrganizationName NVARCHAR(255) NOT NULL,
    ContactPerson NVARCHAR(255) NULL,
    ContactPhone NVARCHAR(20) NULL,
    Address NVARCHAR(255) NULL,
    Website NVARCHAR(255) NULL,
    Description NVARCHAR(MAX) NULL, -- Mô tả về sứ mệnh/hoạt động
    IsVerified BIT DEFAULT 0, -- Cần quy trình xác thực bởi Admin
    VerifiedByAdminId INT NULL, -- FK tới UserId của Admin
    VerificationTime DATETIME2 NULL,
    CreatedAt DATETIME2 DEFAULT GETDATE(),
    UpdatedAt DATETIME2 DEFAULT GETDATE(),
    FOREIGN KEY (UserId) REFERENCES Users(UserId),
    FOREIGN KEY (VerifiedByAdminId) REFERENCES Users(UserId)
);
GO

-- Bảng Skills (Kỹ năng)
CREATE TABLE Skills (
    SkillId INT PRIMARY KEY IDENTITY(1,1),
    SkillName NVARCHAR(100) NOT NULL UNIQUE
);
GO

-- Bảng Causes (Lĩnh vực hoạt động)
CREATE TABLE Causes (
    CauseId INT PRIMARY KEY IDENTITY(1,1),
    CauseName NVARCHAR(100) NOT NULL UNIQUE
);
GO

-- Bảng VolunteerSkills (Kỹ năng của TNV)
CREATE TABLE VolunteerSkills (
    VolunteerSkillId INT PRIMARY KEY IDENTITY(1,1),
    UserId INT NOT NULL,
    SkillId INT NOT NULL,
    FOREIGN KEY (UserId) REFERENCES Users(UserId),
    FOREIGN KEY (SkillId) REFERENCES Skills(SkillId),
    UNIQUE (UserId, SkillId) -- Đảm bảo một TNV chỉ có một kỹ năng cụ thể một lần
);
GO

-- Bảng VolunteerCauses (Lĩnh vực quan tâm của TNV)
CREATE TABLE VolunteerCauses (
    VolunteerCauseId INT PRIMARY KEY IDENTITY(1,1),
    UserId INT NOT NULL,
    CauseId INT NOT NULL,
    FOREIGN KEY (UserId) REFERENCES Users(UserId),
    FOREIGN KEY (CauseId) REFERENCES Causes(CauseId),
    UNIQUE (UserId, CauseId) -- Đảm bảo một TNV chỉ có một lĩnh vực quan tâm cụ thể một lần
);
GO

-- Bảng Opportunities (Cơ hội tình nguyện)
CREATE TABLE Opportunities (
    OpportunityId INT PRIMARY KEY IDENTITY(1,1),
    OrganizationId INT NOT NULL,
    Title NVARCHAR(255) NOT NULL,
    Description NVARCHAR(MAX) NOT NULL,
    CauseId INT NOT NULL,
    Location NVARCHAR(255) NULL,
    StartDate DATETIME2 NULL,
    EndDate DATETIME2 NULL,
    IsFlexibleTime BIT DEFAULT 0, -- Thêm trường này để đánh dấu thời gian linh hoạt/dài hạn
    RequiredVolunteers INT NULL,
    Benefits NVARCHAR(MAX) NULL, -- Lợi ích TNV nhận được
    ContactInfo NVARCHAR(255) NULL,
    ApplicationDeadline DATE NULL,
    Status NVARCHAR(50) DEFAULT 'Open', -- 'Open', 'Closed', 'Completed', 'Cancelled'
    IsApprovedByAdmin BIT DEFAULT 0, -- Cơ hội cần được Admin duyệt
    CreatedAt DATETIME2 DEFAULT GETDATE(),
    UpdatedAt DATETIME2 DEFAULT GETDATE(),
    FOREIGN KEY (OrganizationId) REFERENCES Organizations(OrganizationId),
    FOREIGN KEY (CauseId) REFERENCES Causes(CauseId)
);
GO

-- Bảng OpportunitySkills (Kỹ năng yêu cầu cho Cơ hội)
CREATE TABLE OpportunitySkills (
    OpportunitySkillId INT PRIMARY KEY IDENTITY(1,1),
    OpportunityId INT NOT NULL,
    SkillId INT NOT NULL,
    FOREIGN KEY (OpportunityId) REFERENCES Opportunities(OpportunityId),
    FOREIGN KEY (SkillId) REFERENCES Skills(SkillId),
    UNIQUE (OpportunityId, SkillId) -- Đảm bảo một kỹ năng chỉ được yêu cầu một lần cho một cơ hội
);
GO

-- Bảng Applications (Đơn đăng ký của TNV)
CREATE TABLE Applications (
    ApplicationId INT PRIMARY KEY IDENTITY(1,1),
    OpportunityId INT NOT NULL,
    VolunteerUserId INT NOT NULL,
    ApplicationTime DATETIME2 DEFAULT GETDATE(),
    MotivationLetter NVARCHAR(MAX) NULL,
    Status NVARCHAR(50) DEFAULT 'Pending', -- 'Pending', 'Accepted', 'Rejected', 'Withdrawn', 'Attended', 'Completed'
    OrganizationNotes NVARCHAR(MAX) NULL, -- Ghi chú của Tổ chức khi duyệt
    FOREIGN KEY (OpportunityId) REFERENCES Opportunities(OpportunityId),
    FOREIGN KEY (VolunteerUserId) REFERENCES Users(UserId)
);
GO

-- Bảng Feedback (Đánh giá)
CREATE TABLE Feedback (
    FeedbackId INT PRIMARY KEY IDENTITY(1,1),
    ApplicationId INT NULL, -- Liên kết với đơn đăng ký nếu feedback liên quan trực tiếp đến hoạt động đó
    RaterUserId INT NOT NULL, -- Người đánh giá (có thể là TNV hoặc Tổ chức)
    RatedUserId INT NULL, -- Người được đánh giá (nếu là TNV)
    RatedOrganizationId INT NULL, -- Tổ chức được đánh giá (nếu là Tổ chức)
    Score INT NULL, -- Điểm đánh giá (ví dụ: 1-5 sao)
    Comment NVARCHAR(MAX) NULL,
    FeedbackTime DATETIME2 DEFAULT GETDATE(),
    FOREIGN KEY (ApplicationId) REFERENCES Applications(ApplicationId),
    FOREIGN KEY (RaterUserId) REFERENCES Users(UserId),
    FOREIGN KEY (RatedUserId) REFERENCES Users(UserId),
    FOREIGN KEY (RatedOrganizationId) REFERENCES Organizations(OrganizationId)
);
GO

-- Trigger để tự động cập nhật UpdatedAt cho bảng Users (ví dụ)
CREATE TRIGGER TR_Users_UpdatedAt
ON Users
AFTER UPDATE
AS
BEGIN
    UPDATE Users
    SET UpdatedAt = GETDATE()
    FROM Users u
    INNER JOIN Inserted i ON u.UserId = i.UserId;
END;
GO

-- Bạn có thể tạo các trigger tương tự cho các bảng khác cần theo dõi UpdatedAt


-----------------------------------------------------------------------
------------------------------------------------------------------------
-------------------------------------------------------------------------
-- Đảm bảo đang sử dụng đúng database trước khi chạy các lệnh INSERT
USE HanoConnectDB;
GO

-- 1. Bảng Roles
PRINT 'Inserting data into Roles table...';
INSERT INTO Roles (RoleName) VALUES ('Admin');
INSERT INTO Roles (RoleName) VALUES ('Volunteer');
INSERT INTO Roles (RoleName) VALUES ('Organization');
PRINT 'Roles data inserted.';
GO

-- 2. Bảng Users
PRINT 'Inserting data into Users table...';
-- Admin
INSERT INTO Users (Email, PasswordHash, FullName, PhoneNumber, DateOfBirth, District)
VALUES ('admin@haconnect.vn', 'hashed_password_admin', N'Quản trị viên Hệ thống', '0912345678', '1990-01-01', N'Hoàn Kiếm');

-- Volunteers
INSERT INTO Users (Email, PasswordHash, FullName, PhoneNumber, DateOfBirth, District)
VALUES ('volunteer1@example.com', 'hashed_password_vol1', N'Nguyễn Thị A', '0987654321', '1995-05-10', N'Đống Đa');

INSERT INTO Users (Email, PasswordHash, FullName, PhoneNumber, DateOfBirth, District)
VALUES ('volunteer2@example.com', 'hashed_password_vol2', N'Trần Văn B', '0976543210', '1998-11-20', N'Cầu Giấy');

INSERT INTO Users (Email, PasswordHash, FullName, PhoneNumber, DateOfBirth, District)
VALUES ('volunteer3@example.com', 'hashed_password_vol3', N'Lê Thu Cúc', '0965432109', '2000-03-15', N'Hai Bà Trưng');

-- Organizations
INSERT INTO Users (Email, PasswordHash, FullName, PhoneNumber)
VALUES ('org1@example.com', 'hashed_password_org1', N'Người liên hệ Tổ chức Xanh', '0901234567');

INSERT INTO Users (Email, PasswordHash, FullName, PhoneNumber)
VALUES ('org2@example.com', 'hashed_password_org2', N'Người liên hệ Quỹ Ước Mơ', '0902345678');

-- Organization pending verification
INSERT INTO Users (Email, PasswordHash, FullName, PhoneNumber)
VALUES ('org_pending@example.com', 'hashed_password_org_pending', N'Người liên hệ Tổ chức Mới', '0903456789');
PRINT 'Users data inserted.';
GO

-- 3. Bảng UserRoles (Giả định UserId theo thứ tự insert của Users)
PRINT 'Inserting data into UserRoles table...';
INSERT INTO UserRoles (UserId, RoleId) VALUES ((SELECT UserId FROM Users WHERE Email = 'admin@haconnect.vn'), (SELECT RoleId FROM Roles WHERE RoleName = 'Admin'));
INSERT INTO UserRoles (UserId, RoleId) VALUES ((SELECT UserId FROM Users WHERE Email = 'volunteer1@example.com'), (SELECT RoleId FROM Roles WHERE RoleName = 'Volunteer'));
INSERT INTO UserRoles (UserId, RoleId) VALUES ((SELECT UserId FROM Users WHERE Email = 'volunteer2@example.com'), (SELECT RoleId FROM Roles WHERE RoleName = 'Volunteer'));
INSERT INTO UserRoles (UserId, RoleId) VALUES ((SELECT UserId FROM Users WHERE Email = 'volunteer3@example.com'), (SELECT RoleId FROM Roles WHERE RoleName = 'Volunteer'));
INSERT INTO UserRoles (UserId, RoleId) VALUES ((SELECT UserId FROM Users WHERE Email = 'org1@example.com'), (SELECT RoleId FROM Roles WHERE RoleName = 'Organization'));
INSERT INTO UserRoles (UserId, RoleId) VALUES ((SELECT UserId FROM Users WHERE Email = 'org2@example.com'), (SELECT RoleId FROM Roles WHERE RoleName = 'Organization'));
INSERT INTO UserRoles (UserId, RoleId) VALUES ((SELECT UserId FROM Users WHERE Email = 'org_pending@example.com'), (SELECT RoleId FROM Roles WHERE RoleName = 'Organization'));
PRINT 'UserRoles data inserted.';
GO

-- 4. Bảng Organizations
PRINT 'Inserting data into Organizations table...';
INSERT INTO Organizations (UserId, OrganizationName, ContactPerson, ContactPhone, Address, Website, Description, IsVerified, VerifiedByAdminId, VerificationTime)
VALUES (
    (SELECT UserId FROM Users WHERE Email = 'org1@example.com'),
    N'Tổ chức Bảo vệ Môi trường Xanh Hà Nội',
    N'Người liên hệ Tổ chức Xanh',
    '02412345678',
    N'Số 10, Ngõ 1, Đường Xanh, Đống Đa, Hà Nội',
    'https://tochucxanhhanoi.org',
    N'Chúng tôi cam kết bảo vệ và cải thiện môi trường sống tại Hà Nội thông qua các hoạt động thu gom rác, trồng cây và nâng cao nhận thức cộng đồng.',
    1, -- Đã được xác thực
    (SELECT UserId FROM Users WHERE Email = 'admin@haconnect.vn'), -- Admin duyệt
    GETDATE()
);

INSERT INTO Organizations (UserId, OrganizationName, ContactPerson, ContactPhone, Address, Website, Description, IsVerified, VerifiedByAdminId, VerificationTime)
VALUES (
    (SELECT UserId FROM Users WHERE Email = 'org2@example.com'),
    N'Quỹ Ước Mơ Cho Trẻ Em',
    N'Người liên hệ Quỹ Ước Mơ',
    '02498765432',
    N'Số 20, Phố Hạnh Phúc, Hai Bà Trưng, Hà Nội',
    'https://quyocmo.org',
    N'Quỹ Ước Mơ tập trung vào việc hỗ trợ giáo dục và chăm sóc sức khỏe cho trẻ em có hoàn cảnh khó khăn tại Hà Nội.',
    1, -- Đã được xác thực
    (SELECT UserId FROM Users WHERE Email = 'admin@haconnect.vn'), -- Admin duyệt
    GETDATE()
);

INSERT INTO Organizations (UserId, OrganizationName, ContactPerson, ContactPhone, Address, Website, Description, IsVerified)
VALUES (
    (SELECT UserId FROM Users WHERE Email = 'org_pending@example.com'),
    N'Tổ chức Tình nguyện Cộng đồng ABC',
    N'Người liên hệ Tổ chức Mới',
    '02456789012',
    N'Số 30, Ngõ Vui Vẻ, Long Biên, Hà Nội',
    'https://tochucabc.org',
    N'Hỗ trợ các hoạt động cộng đồng nhỏ lẻ tại các khu dân cư.',
    0 -- Chưa được xác thực
);
PRINT 'Organizations data inserted.';
GO

-- 5. Bảng Skills
PRINT 'Inserting data into Skills table...';
INSERT INTO Skills (SkillName) VALUES (N'Dạy học');
INSERT INTO Skills (SkillName) VALUES (N'Thiết kế đồ họa');
INSERT INTO Skills (SkillName) VALUES (N'IT / Lập trình');
INSERT INTO Skills (SkillName) VALUES (N'Tổ chức sự kiện');
INSERT INTO Skills (SkillName) VALUES (N'Chăm sóc sức khỏe');
INSERT INTO Skills (SkillName) VALUES (N'Truyền thông / Marketing');
INSERT INTO Skills (SkillName) VALUES (N'Biên phiên dịch');
INSERT INTO Skills (SkillName) VALUES (N'Vận chuyển / Hậu cần');
INSERT INTO Skills (SkillName) VALUES (N'Chụp ảnh / Quay phim');
PRINT 'Skills data inserted.';
GO

-- 6. Bảng Causes
PRINT 'Inserting data into Causes table...';
INSERT INTO Causes (CauseName) VALUES (N'Bảo vệ Môi trường');
INSERT INTO Causes (CauseName) VALUES (N'Giáo dục');
INSERT INTO Causes (CauseName) VALUES (N'Chăm sóc Trẻ em');
INSERT INTO Causes (CauseName) VALUES (N'Hỗ trợ Người già');
INSERT INTO Causes (CauseName) VALUES (N'Y tế và Sức khỏe');
INSERT INTO Causes (CauseName) VALUES (N'Văn hóa và Nghệ thuật');
INSERT INTO Causes (CauseName) VALUES (N'Phát triển Cộng đồng');
INSERT INTO Causes (CauseName) VALUES (N'Cứu trợ thiên tai');
PRINT 'Causes data inserted.';
GO

-- 7. Bảng VolunteerSkills
PRINT 'Inserting data into VolunteerSkills table...';
-- Volunteer 1 (Nguyễn Thị A - IT, Dạy học)
INSERT INTO VolunteerSkills (UserId, SkillId) VALUES (
    (SELECT UserId FROM Users WHERE Email = 'volunteer1@example.com'),
    (SELECT SkillId FROM Skills WHERE SkillName = N'IT / Lập trình')
);
INSERT INTO VolunteerSkills (UserId, SkillId) VALUES (
    (SELECT UserId FROM Users WHERE Email = 'volunteer1@example.com'),
    (SELECT SkillId FROM Skills WHERE SkillName = N'Dạy học')
);

-- Volunteer 2 (Trần Văn B - Tổ chức sự kiện, Truyền thông)
INSERT INTO VolunteerSkills (UserId, SkillId) VALUES (
    (SELECT UserId FROM Users WHERE Email = 'volunteer2@example.com'),
    (SELECT SkillId FROM Skills WHERE SkillName = N'Tổ chức sự kiện')
);
INSERT INTO VolunteerSkills (UserId, SkillId) VALUES (
    (SELECT UserId FROM Users WHERE Email = 'volunteer2@example.com'),
    (SELECT SkillId FROM Skills WHERE SkillName = N'Truyền thông / Marketing')
);

-- Volunteer 3 (Lê Thu Cúc - Chăm sóc sức khỏe)
INSERT INTO VolunteerSkills (UserId, SkillId) VALUES (
    (SELECT UserId FROM Users WHERE Email = 'volunteer3@example.com'),
    (SELECT SkillId FROM Skills WHERE SkillName = N'Chăm sóc sức khỏe')
);
PRINT 'VolunteerSkills data inserted.';
GO

-- 8. Bảng VolunteerCauses
PRINT 'Inserting data into VolunteerCauses table...';
-- Volunteer 1 (Nguyễn Thị A - Giáo dục, Môi trường)
INSERT INTO VolunteerCauses (UserId, CauseId) VALUES (
    (SELECT UserId FROM Users WHERE Email = 'volunteer1@example.com'),
    (SELECT CauseId FROM Causes WHERE CauseName = N'Giáo dục')
);
INSERT INTO VolunteerCauses (UserId, CauseId) VALUES (
    (SELECT UserId FROM Users WHERE Email = 'volunteer1@example.com'),
    (SELECT CauseId FROM Causes WHERE CauseName = N'Bảo vệ Môi trường')
);

-- Volunteer 2 (Trần Văn B - Phát triển Cộng đồng)
INSERT INTO VolunteerCauses (UserId, CauseId) VALUES (
    (SELECT UserId FROM Users WHERE Email = 'volunteer2@example.com'),
    (SELECT CauseId FROM Causes WHERE CauseName = N'Phát triển Cộng đồng')
);

-- Volunteer 3 (Lê Thu Cúc - Chăm sóc Trẻ em, Y tế)
INSERT INTO VolunteerCauses (UserId, CauseId) VALUES (
    (SELECT UserId FROM Users WHERE Email = 'volunteer3@example.com'),
    (SELECT CauseId FROM Causes WHERE CauseName = N'Chăm sóc Trẻ em')
);
INSERT INTO VolunteerCauses (UserId, CauseId) VALUES (
    (SELECT UserId FROM Users WHERE Email = 'volunteer3@example.com'),
    (SELECT CauseId FROM Causes WHERE CauseName = N'Y tế và Sức khỏe')
);
PRINT 'VolunteerCauses data inserted.';
GO

-- 9. Bảng Opportunities
PRINT 'Inserting data into Opportunities table...';
-- Cơ hội từ Tổ chức Xanh Hà Nội (Môi trường)
INSERT INTO Opportunities (OrganizationId, Title, Description, CauseId, Location, StartDate, EndDate, RequiredVolunteers, Benefits, ApplicationDeadline, Status, IsApprovedByAdmin)
VALUES (
    (SELECT OrganizationId FROM Organizations WHERE OrganizationName = N'Tổ chức Bảo vệ Môi trường Xanh Hà Nội'),
    N'Chiến dịch Làm sạch Hồ Tây 2025',
    N'Tham gia thu gom rác thải, làm sạch khu vực Hồ Tây và các công viên lân cận nhằm nâng cao nhận thức cộng đồng về bảo vệ môi trường.',
    (SELECT CauseId FROM Causes WHERE CauseName = N'Bảo vệ Môi trường'),
    N'Khu vực Hồ Tây, Hà Nội',
    '2025-07-20 08:00:00',
    '2025-07-20 17:00:00',
    50,
    N'Giấy chứng nhận tham gia, bữa trưa, áo đồng phục',
    '2025-07-15',
    'Open',
    1 -- Đã được Admin duyệt
);

-- Cơ hội từ Quỹ Ước Mơ Cho Trẻ Em (Giáo dục)
INSERT INTO Opportunities (OrganizationId, Title, Description, CauseId, Location, StartDate, EndDate, IsFlexibleTime, RequiredVolunteers, Benefits, ApplicationDeadline, Status, IsApprovedByAdmin)
VALUES (
    (SELECT OrganizationId FROM Organizations WHERE OrganizationName = N'Quỹ Ước Mơ Cho Trẻ Em'),
    N'Dạy kèm tiếng Anh cho trẻ em khó khăn',
    N'Dạy kèm tiếng Anh cơ bản cho các em nhỏ tại trung tâm bảo trợ xã hội. Hoạt động diễn ra linh hoạt 2 buổi/tuần.',
    (SELECT CauseId FROM Causes WHERE CauseName = N'Giáo dục'),
    N'Trung tâm Bảo trợ Trẻ em Hà Nội, Nam Từ Liêm',
    '2025-08-01 09:00:00',
    '2025-12-31 17:00:00',
    1, -- Đánh dấu là thời gian linh hoạt/dài hạn
    10,
    N'Chứng nhận hoàn thành chương trình, cơ hội giao lưu văn hóa',
    '2025-07-25',
    'Open',
    1
);

-- Cơ hội từ Tổ chức Bảo vệ Môi trường Xanh Hà Nội (Đã đóng)
INSERT INTO Opportunities (OrganizationId, Title, Description, CauseId, Location, StartDate, EndDate, RequiredVolunteers, Benefits, ApplicationDeadline, Status, IsApprovedByAdmin)
VALUES (
    (SELECT OrganizationId FROM Organizations WHERE OrganizationName = N'Tổ chức Bảo vệ Môi trường Xanh Hà Nội'),
    N'Trồng cây gây rừng tại Sóc Sơn',
    N'Hoạt động trồng 1000 cây xanh tại khu vực Sóc Sơn nhằm cải thiện chất lượng không khí.',
    (SELECT CauseId FROM Causes WHERE CauseName = N'Bảo vệ Môi trường'),
    N'Sóc Sơn, Hà Nội',
    '2025-06-01 07:00:00',
    '2025-06-01 16:00:00',
    30,
    N'Giấy chứng nhận, phương tiện đi lại',
    '2025-05-20',
    'Completed', -- Đã hoàn thành
    1
);
PRINT 'Opportunities data inserted.';
GO

-- 10. Bảng OpportunitySkills
PRINT 'Inserting data into OpportunitySkills table...';
-- Dạy kèm tiếng Anh cho trẻ em khó khăn: Yêu cầu Dạy học
INSERT INTO OpportunitySkills (OpportunityId, SkillId) VALUES (
    (SELECT OpportunityId FROM Opportunities WHERE Title = N'Dạy kèm tiếng Anh cho trẻ em khó khăn'),
    (SELECT SkillId FROM Skills WHERE SkillName = N'Dạy học')
);
PRINT 'OpportunitySkills data inserted.';
GO

-- 11. Bảng Applications
PRINT 'Inserting data into Applications table...';
-- Volunteer 1 đăng ký "Chiến dịch Làm sạch Hồ Tây 2025" (Đã chấp nhận)
INSERT INTO Applications (OpportunityId, VolunteerUserId, ApplicationTime, MotivationLetter, Status)
VALUES (
    (SELECT OpportunityId FROM Opportunities WHERE Title = N'Chiến dịch Làm sạch Hồ Tây 2025'),
    (SELECT UserId FROM Users WHERE Email = 'volunteer1@example.com'),
    GETDATE(),
    N'Em rất mong muốn được đóng góp sức mình vào việc bảo vệ môi trường, đặc biệt là khu vực Hồ Tây mà em rất yêu quý. Em có sức khỏe tốt và tinh thần nhiệt huyết.',
    'Accepted'
);

-- Volunteer 2 đăng ký "Dạy kèm tiếng Anh cho trẻ em khó khăn" (Chờ duyệt)
INSERT INTO Applications (OpportunityId, VolunteerUserId, ApplicationTime, MotivationLetter, Status)
VALUES (
    (SELECT OpportunityId FROM Opportunities WHERE Title = N'Dạy kèm tiếng Anh cho trẻ em khó khăn'),
    (SELECT UserId FROM Users WHERE Email = 'volunteer2@example.com'),
    GETDATE(),
    N'Em có kinh nghiệm dạy kèm tiếng Anh cho các em nhỏ và rất yêu thích công việc này. Mong rằng sẽ có cơ hội được đồng hành cùng Quỹ Ước Mơ.',
    'Pending'
);

-- Volunteer 3 đăng ký "Chiến dịch Làm sạch Hồ Tây 2025" (Chờ duyệt)
INSERT INTO Applications (OpportunityId, VolunteerUserId, ApplicationTime, Status)
VALUES (
    (SELECT OpportunityId FROM Opportunities WHERE Title = N'Chiến dịch Làm sạch Hồ Tây 2025'),
    (SELECT UserId FROM Users WHERE Email = 'volunteer3@example.com'),
    GETDATE(),
    'Pending'
);

-- Volunteer 1 cũng đăng ký "Trồng cây gây rừng tại Sóc Sơn" (Đã hoàn thành)
INSERT INTO Applications (OpportunityId, VolunteerUserId, ApplicationTime, Status)
VALUES (
    (SELECT OpportunityId FROM Opportunities WHERE Title = N'Trồng cây gây rừng tại Sóc Sơn'),
    (SELECT UserId FROM Users WHERE Email = 'volunteer1@example.com'),
    '2025-05-25 10:00:00',
    'Completed'
);
PRINT 'Applications data inserted.';
GO