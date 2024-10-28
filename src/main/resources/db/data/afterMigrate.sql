CREATE EXTENSION IF NOT EXISTS pgcrypto;
SET session_replication_role = 'replica';
DELETE FROM usuario;
DELETE FROM aluno;
DELETE FROM presenca;
DELETE FROM horario;

INSERT INTO usuario(id, nome, email, senha, codigo_recuperacao, cargo) VALUES
    (gen_random_uuid(), 'admin', 'admin@example.com', crypt('123', gen_salt('bf')), null, 0),
    (gen_random_uuid(), 'instrutor', 'instrutor@example.com', crypt('123', gen_salt('bf')), null, 1),
    (gen_random_uuid(), 'estagiario', 'estagiario@example.com', crypt('123', gen_salt('bf')), null, 2);

INSERT INTO aluno(id, turno_id, colocacao, nome, email, telefone, cirurgias, patologias, meses_experiencia_musculacao, diagnostico_lesao_joelho, status, ausencias_consecutivas, altura, peso, lista_espera) VALUES
    (gen_random_uuid(), null, null, 'Example 1', 'user1@example.com','1140028922', 'Não tem', 'Sim', 5, 'Não tem', 0, 1, 155, 54.2, false),
    (gen_random_uuid(), null, null, 'Example 2', 'user2@example.com', '11987654321', 'Sim', 'Não', 12, 'Não tem', 0, 2, 172, 68.5, false),
    (gen_random_uuid(), null, null, 'Example 3', 'user3@example.com', '11876543210', 'Não tem', 'Sim', 24, 'Sim', 1, 4, 160, 62.0, false),
    (gen_random_uuid(), null, null, 'Example 4', 'user4@example.com', '11765432109', 'Não tem', 'Não', 8, 'Sim', 1, 2, 178, 85.3, false),
    (gen_random_uuid(), null, null, 'Example 5', 'user5@example.com', '11654321098', 'Sim', 'Não', 15, 'Não tem', 0, 0, 165, 70.8, false),
    (gen_random_uuid(), null, null, 'Example 6', 'user6@example.com', '11543210987', 'Não tem', 'Sim', 6, 'Não tem', 0, 1, 182, 90.5, false),
    (gen_random_uuid(), null, null, 'Example 7', 'user7@example.com', '11432109876', 'Sim', 'Não', 10, 'Não tem', 0, 2, 170, 65.2, false),
    (gen_random_uuid(), null, null, 'Example 8', 'user8@example.com', '11321098765', 'Não tem', 'Sim', 20, 'Sim', 1, 3, 175, 77.6, false),
    (gen_random_uuid(), null, null, 'Example 9', 'user9@example.com', '11210987654', 'Sim', 'Não', 18, 'Não tem', 0, 0, 168, 72.4, false),
    (gen_random_uuid(), null, null, 'Example 10', 'user10@example.com', '11109876543', 'Não tem', 'Sim', 7, 'Sim', 0, 0, 180, 80.1, false),
    (gen_random_uuid(), null, 1, 'Example 11', 'user11@example.com', null, null, null, null, null, null, null, null, null, true),
    (gen_random_uuid(), null, 2, 'Example 12', 'user12@example.com', null, null, null, null, null, null, null, null, null, true),
    (gen_random_uuid(), null, 3, 'Example 13', 'user13@example.com', null, null, null, null, null, null, null, null, null, true),
    (gen_random_uuid(), null, 4, 'Example 14', 'user14@example.com', null, null, null, null, null, null, null, null, null, true),
    (gen_random_uuid(), null, 5, 'Example 15', 'user15@example.com', null, null, null, null, null, null, null, null, null, true),
    (gen_random_uuid(), null, 6, 'Example 16', 'user16@example.com', null, null, null, null, null, null, null, null, null, true),
    (gen_random_uuid(), null, 7, 'Example 17', 'user17@example.com', null, null, null, null, null, null, null, null, null, true),
    (gen_random_uuid(), null, 8, 'Example 18', 'user18@example.com', null, null, null, null, null, null, null, null, null, true),
    (gen_random_uuid(), null, 9, 'Example 19', 'user19@example.com', null, null, null, null, null, null, null, null, null, true),
    (gen_random_uuid(), null, 10, 'Example 20', 'user20@example.com', null, null, null, null, null, null, null, null, null, true);

INSERT INTO horario (id, horario_inicial, horario_final, aluno_id, turno, vagas_disponiveis) VALUES
    ('6667a4c1-9bfd-422e-9dbc-602f8d0c8e27', '07:00:00', '08:00:00', null, 'MANHA', 15),
    ('56e88ae8-6a11-468a-a6fb-08cd2ac07a0b', '08:00:00', '09:00:00', null, 'MANHA', 15),
    ('fe2baa3a-59fc-43c7-a5b2-8e3e872471d7', '09:00:00', '10:00:00', null, 'MANHA', 15),
    ('2f2866be-4738-4602-b1ea-27da45143b6c', '10:00:00', '11:00:00', null, 'MANHA', 15),
    ('1bfac9c7-4652-48bb-b4b7-46983225831b', '11:00:00', '12:00:00', null, 'MANHA', 15),
    ('cc738671-91f2-4c41-8575-ad928f5ef892', '12:00:00', '13:00:00', null, 'TARDE', 15),
    ('b5e51f92-03a9-40a0-9f05-2ea211703d3c', '13:00:00', '14:00:00', null, 'TARDE', 15),
    ('3e291362-6377-43a6-a3a7-bf73918f5120', '14:00:00', '15:00:00', null, 'TARDE', 15),
    ('a780503d-88ff-4541-903f-f033f45f42c2', '15:00:00', '16:00:00', null, 'TARDE', 15),
    ('4efea951-827d-4736-b399-668525227873', '16:00:00', '17:00:00', null, 'TARDE', 15),
    ('6f160df3-f6d0-4e96-99fd-36c4b0ad23cd', '17:00:00', '18:00:00', null, 'NOITE', 15),
    ('0f0fd5be-b0c2-4726-953a-68fe80ffc434', '18:00:00', '19:00:00', null, 'NOITE', 15),
    ('dc93003f-59b9-4b4c-b8dd-9681a7c6d8a8', '19:00:00', '20:00:00', null, 'NOITE', 15),
    ('d47d73f0-70db-4cb1-aa1b-243d363b476b', '20:00:00', '21:00:00', null, 'NOITE', 15);


SET session_replication_role = 'origin';
