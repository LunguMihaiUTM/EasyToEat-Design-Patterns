INSERT INTO users (id, username, password, email, phone, role)
VALUES (nextval('user_id_seq'), 'admin', 'admin123', 'admin@easytoeat.md', '060000001', 'ADMIN'),
       (nextval('user_id_seq'), 'Mihai1', 'password123', 'mihai1.com', '060000002', 'USER'),
       (nextval('user_id_seq'), 'Mihai2', 'securepass', 'mihai2.com', '060000003', 'USER');
