USE HanoConnectDB; -- Đảm bảo bạn đang ở đúng database
GO

INSERT INTO [__EFMigrationsHistory] ([MigrationId], [ProductVersion])
VALUES (N'20250619154437_InitialCreate', N'8.0.6'); -- Thay '8.0.6' bằng phiên bản EF Core bạn đang dùng

GO