CREATE TABLE IF NOT EXISTS `users`
(
    Id         CHAR(36) PRIMARY KEY DEFAULT (UUID()),
    UserName   VARCHAR(64)  NOT NULL,
    Email      VARCHAR(128) NOT NULL,
    AvatarUrl  VARCHAR(32)  NULL,
    Birthdate  DATETIME     NOT NULL,
    DeleteDate DATETIME     NULL,
    Password   VARCHAR(128) NOT NULL,
    UNIQUE (UserName, Email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `roles`
(
    Id          CHAR(36) PRIMARY KEY DEFAULT (UUID()),
    Name        VARCHAR(256) NOT NULL,
    DisplayName VARCHAR(128) NOT NULL,
    UNIQUE (Name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `userRoles`
(
    UserId CHAR(36) NOT NULL,
    RoleId CHAR(36) NOT NULL,
    FOREIGN KEY (UserId) REFERENCES users (Id),
    FOREIGN KEY (RoleId) REFERENCES roles (Id),
    PRIMARY KEY (UserId, RoleId)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `rates`
(
    ItemId CHAR(36) NOT NULL,
    UserId CHAR(36) NOT NULL,
    FOREIGN KEY (UserId) REFERENCES users (Id),
    PRIMARY KEY (ItemId, UserId)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `sections`
(
    Id         CHAR(36) PRIMARY KEY DEFAULT (UUID()),
    Title      VARCHAR(256) NOT NULL,
    DeleteDate DATETIME     NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `themes`
(
    Id          CHAR(36) PRIMARY KEY DEFAULT (UUID()),
    SectionId   CHAR(36)     NOT NULL,
    Title       VARCHAR(256) NOT NULL,
    Description TEXT         NOT NULL,
    DeleteDate  DATETIME     NULL,
    FOREIGN KEY (SectionId) REFERENCES sections (Id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `posts`
(
    Id          CHAR(36) PRIMARY KEY DEFAULT (UUID()),
    ThemeId     CHAR(36)     NOT NULL,
    AuthorId    CHAR(36)     NOT NULL,
    Title       VARCHAR(256) NOT NULL,
    Description TEXT         NOT NULL,
    CreateDate  DATETIME             DEFAULT CURRENT_TIMESTAMP,
    DeleteDate  DATETIME     NULL,
    FOREIGN KEY (ThemeId) REFERENCES themes (Id),
    FOREIGN KEY (AuthorId) REFERENCES users (Id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;