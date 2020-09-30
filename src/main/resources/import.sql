INSERT INTO roles (name) VALUES ('ADMIN');
INSERT INTO roles (name) VALUES ('USER');
INSERT INTO users (age, email, firstName, lastName, password) VALUES (33, 'admin@mail.com', 'Admin', 'class', 'a');
INSERT INTO users (age, email, firstName, lastName, password) VALUES (25, 'user@test.com', 'User', 'class', 'u');
INSERT INTO users_roles (users_id, roles_id) VALUES (1, 1);
INSERT INTO users_roles (users_id, roles_id) VALUES (1, 2);
INSERT INTO users_roles (users_id, roles_id) VALUES (2, 2);
