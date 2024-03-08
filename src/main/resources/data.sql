INSERT INTO user (user_uuid, email, username, password, encrypted_password, introduce) VALUES
    ('admin-uuid', 'master@example.com', 'master', 'masterPwd9', 'admin-encrypted-password', 'he is master');



INSERT INTO role (role, user_id) VALUES
    ('ROLE_USER', 1);

INSERT INTO role (role, user_id) VALUES
    ('ROLE_ADMIN', 1);
