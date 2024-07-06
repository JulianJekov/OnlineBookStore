USE `book-store`;

INSERT INTO roles (id, name)
VALUES (1, 'ADMIN');
INSERT INTO roles (id, name)
VALUES (2, 'USER');


INSERT INTO users (id, email, password, username, first_name, last_name, age, is_active)
VALUES (1, 'admin@admin.com','$2a$10$zBnFYi0FpI.dBUW6cKzLAOaoHcx8FO83Ieos/zBSwGR48cN39O4WK',
        'admin', 'Admin', 'Adminov', 30, true);

INSERT INTO users (id, email, password, username, first_name, last_name, age, is_active)
VALUES (2, 'user@user.com','$2a$10$zBnFYi0FpI.dBUW6cKzLAOaoHcx8FO83Ieos/zBSwGR48cN39O4WK',
        'user', 'User', 'Userov', 31, true);

-- user roles
-- admin
INSERT INTO users_roles (`user_id`, `roles_id`)
VALUES (1, 1);
INSERT INTO users_roles (`user_id`, `roles_id`)
VALUES (1, 2);
-- user
INSERT INTO users_roles (`user_id`, `roles_id`)
VALUES (2, 2);