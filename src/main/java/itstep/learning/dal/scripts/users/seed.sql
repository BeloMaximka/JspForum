INSERT INTO users(Id, UserName, Email, Birthdate, Password)
    VALUES ('7dd7d8a9-815e-11ef-bb48-fcfbf6dd7098',
    'admin',
    'admin@change.me',
    '1970-01-01',
    '$2a$12$PxJC1FYpn4EgrdqUsk7.MezrjY0.HRFnASo01wCo0Zh3UZRZt3EmC')
    ON DUPLICATE KEY UPDATE UserName = 'admin'