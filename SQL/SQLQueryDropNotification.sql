USE HanoConnectDB;
GO

-- Xóa khóa ngoại trỏ đến bảng Notifications nếu có
-- (Trong trường hợp này không có, nhưng đây là bước phòng ngừa tốt)
-- ALTER TABLE ... DROP CONSTRAINT ...;

-- Xóa bảng Notifications
IF OBJECT_ID('dbo.Notifications', 'U') IS NOT NULL
    DROP TABLE dbo.Notifications;
GO

-- Xóa bản ghi migration liên quan để tránh lỗi sau này
DELETE FROM [__EFMigrationsHistory] WHERE MigrationId LIKE '%AddNotificationsTable%';
GO
