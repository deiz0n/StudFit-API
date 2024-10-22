CREATE EXTENSION IF NOT EXISTS pgcrypto;
SET session_replication_role = 'replica';
DELETE FROM usuario;
DELETE FROM aluno;
DELETE FROM presenca;

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

SET session_replication_role = 'origin';
