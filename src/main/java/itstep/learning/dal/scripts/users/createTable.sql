CREATE TABLE  IF NOT EXISTS `users` (
    Id         CHAR(36)     PRIMARY KEY  DEFAULT(UUID()),
    UserName   VARCHAR(64)  NOT NULL,
    Email      VARCHAR(128) NOT NULL,
    AvatarUrl  VARCHAR(32)      NULL,
    Birthdate  DATETIME     NOT NULL,
    DeleteDate DATETIME         NULL,
    Password   VARCHAR(128) NOT NULL,
    UNIQUE (UserName, Email)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci
