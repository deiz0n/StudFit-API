CREATE EXTENSION IF NOT EXISTS pgcrypto;
SET session_replication_role = 'replica';
DELETE FROM tb_presenca;
DELETE FROM tb_usuario;
DELETE FROM tb_aluno;
DELETE FROM tb_horario;
DELETE FROM tb_aluno_interessado;
DELETE FROM tb_horario_interesse;
DELETE FROM tb_aluno_horario_preferencia;

INSERT INTO tb_usuario(id, nome, email, senha, codigo_recuperacao, cargo) VALUES
    (gen_random_uuid(), 'admin', 'admin@example.com', crypt('123', gen_salt('bf')), null, 'ADMINISTRADOR'),
    ('f1d13d02-9ebe-4f38-b372-faac79e82991', 'instrutor', 'instrutor@example.com', crypt('123', gen_salt('bf')), null, 'INSTRUTOR'),
    ('75b8e85b-2c70-4352-9903-305918c34f34', 'estagiario', 'estagiario@example.com', crypt('123', gen_salt('bf')), null, 'ESTAGIARIO'),
    ('915b6311-2965-4e34-aa82-8f89a1ea4c38', 'estagiario2', 'estagiario2@example.com', crypt('123', gen_salt('bf')), null, 'ESTAGIARIO');

INSERT INTO tb_aluno_horario_preferencia(id) VALUES
    ('74b330d4-026a-46f0-8e67-a49a0c1ad538'),
    ('4ae55060-94be-4b11-b581-ecec6a182312'),
    ('3d515737-4325-469d-911d-0c2f01fccb77'),
    ('d6c6356d-10cd-4263-9c18-a3a254327735'),
    ('7c029484-7656-4558-8fac-56243243ed90'),
    ('cb367047-5929-49f3-87ce-68481ccb45ff'),
    ('f2d1860b-b01d-4690-9e11-1fa3144cd8f6'),
    ('f2b26510-6b7f-4e4b-b6c5-5ff99c85663b'),
    ('43d44fdf-a4fa-48a2-a654-211ba5d2b6c1'),
    ('5434028e-46c6-4c8e-abe1-b00c61039c79'),
    ('61deb934-6dca-4fc5-a10e-734f4932cbe6'),
    ('787695d3-edc7-439a-b28f-74d5095b6baf'),
    ('b8394a55-1514-41c3-ac1f-eae1d1df6a2d'),
    ('e7534535-1afc-46bb-a1c7-df8c75c637f8'),
    ('c9e86dee-812a-4b10-8549-7de9b38d5095');

INSERT INTO tb_horario_interesse (id, aluno_horario_preferencia_id) VALUES
    ('74b330d4-026a-46f0-8e67-a49a0c1ad5ed', '74b330d4-026a-46f0-8e67-a49a0c1ad538'),
    ('4ae55060-94be-4b11-b581-ecec6a18dd66', '4ae55060-94be-4b11-b581-ecec6a182312'),
    ('8428647e-dfc3-47a1-8e74-23cfee698546', '3d515737-4325-469d-911d-0c2f01fccb77'),
    ('d6c6356d-10cd-4263-9c18-a3a275127735', 'd6c6356d-10cd-4263-9c18-a3a254327735'),
    ('7c029484-7656-4558-8fac-56fc74fed904', '7c029484-7656-4558-8fac-56243243ed90'),
    ('cb367047-5929-49f3-87ce-68481ccb84fb', 'cb367047-5929-49f3-87ce-68481ccb45ff'),
    ('f20b4d97-3743-4147-90b9-464ed48b9a95', 'f2d1860b-b01d-4690-9e11-1fa3144cd8f6'),
    ('e16d77d4-fafa-4ccc-abe6-1d2f86e4a3a4', 'f2b26510-6b7f-4e4b-b6c5-5ff99c85663b'),
    ('2ad44fdf-a4fa-48a2-a654-211ba5d2b6c1', '43d44fdf-a4fa-48a2-a654-211ba5d2b6c1'),
    ('1d3028ee-46c6-4c8e-abe1-b00c61039c79', null),
    ('6106b934-6dca-4fc5-a10e-734f4932cbe6', null),
    ('dedfadac-2d3a-4167-8318-3ed479a6a4c9', null),
    ('b5fac5dd-141b-4117-8b38-9d00011ef8e1', null),
    ('e752b035-1afc-469e-a1c7-df8c75c637f8', null),
    ('c9e86dee-812a-4b10-8549-7ce9b38d5095', null);

INSERT INTO tb_horario (id, horario_inicial, horario_final, horario_interesse_id, turno, vagas_disponiveis) VALUES
    ('6667a4c1-9bfd-422e-9dbc-602f8d0c8e27', '07:00:00', '08:00:00', null, 'MANHA', 14),
    ('56e88ae8-6a11-468a-a6fb-08cd2ac07a0b', '08:00:00', '09:00:00', null, 'MANHA', 14),
    ('fe2baa3a-59fc-43c7-a5b2-8e3e872471d7', '09:00:00', '10:00:00', null, 'MANHA', 14),
    ('2f2866be-4738-4602-b1ea-27da45143b6c', '10:00:00', '11:00:00', null, 'MANHA', 14),
    ('1bfac9c7-4652-48bb-b4b7-46983225831b', '11:00:00', '12:00:00', null, 'MANHA', 14),
    ('cc738671-91f2-4c41-8575-ad928f5ef892', '12:00:00', '13:00:00', null, 'TARDE', 15),
    ('b5e51f92-03a9-40a0-9f05-2ea211703d3c', '13:00:00', '14:00:00', null, 'TARDE', 15),
    ('3e291362-6377-43a6-a3a7-bf73918f5120', '14:00:00', '15:00:00', null, 'TARDE', 13),
    ('a780503d-88ff-4541-903f-f033f45f42c2', '15:00:00', '16:00:00', null, 'TARDE', 12),
    ('4efea951-827d-4736-b399-668525227873', '16:00:00', '17:00:00', null, 'TARDE', 14),
    ('6f160df3-f6d0-4e96-99fd-36c4b0ad23cd', '17:00:00', '18:00:00', null, 'NOITE', 14),
    ('0f0fd5be-b0c2-4726-953a-68fe80ffc434', '18:00:00', '19:00:00', null, 'NOITE', 14),
    ('dc93003f-59b9-4b4c-b8dd-9681a7c6d8a8', '19:00:00', '20:00:00', null, 'NOITE', 14),
    ('d47d73f0-70db-4cb1-aa1b-243d363b476b', '20:00:00', '21:00:00', null, 'NOITE', 9);

INSERT INTO tb_aluno_interessado(id, aluno_horario_preferencia_id) VALUES
    ('b4e876c4-a977-442f-a746-7317de9d70e2', '74b330d4-026a-46f0-8e67-a49a0c1ad538'),
    ('7098316a-87f0-457f-936b-79e5d538b69d', '4ae55060-94be-4b11-b581-ecec6a182312'),
    ('d9b9f322-4c2d-40e7-aee8-a46048c49c1d', '3d515737-4325-469d-911d-0c2f01fccb77'),
    ('8307c2da-aeb7-4f25-94a0-edcf820b0e59', 'd6c6356d-10cd-4263-9c18-a3a254327735'),
    ('5002366d-3344-4ffc-b038-c7a9dc711a77', '7c029484-7656-4558-8fac-56243243ed90'),
    ('0d8188fe-1e01-4cda-bd9b-56d77ce1cd07', 'cb367047-5929-49f3-87ce-68481ccb45ff'),
    ('5af35073-ccf6-4986-b31f-53f1c29ac821', 'f2d1860b-b01d-4690-9e11-1fa3144cd8f6'),
    ('6f501097-2c63-4469-bf5f-7abe1d62d615', 'f2b26510-6b7f-4e4b-b6c5-5ff99c85663b'),
    ('e2389532-f419-4243-9d0a-edd0245845cd', '43d44fdf-a4fa-48a2-a654-211ba5d2b6c1');

INSERT INTO tb_aluno(id, colocacao, nome, email, telefone, cirurgias, patologias, meses_experiencia_musculacao, diagnostico_lesao_joelho, status, ausencias_consecutivas, altura, peso, lista_espera, horario_id, consumo_alcool, consumo_cigarro, pratica_exercicio_fisico, aluno_interessado_id, turnos_preferenciais) VALUES
    ('00d67ac7-a1e9-4abd-a393-8464b77c0c9e',  null, 'Example 1', 'user1@example.com','1140028922', 'Não tem', 'Sim', 5, 'Não tem', 'REGULAR', 2, 155, 54.2, false, '6667a4c1-9bfd-422e-9dbc-602f8d0c8e27', true, false, true, null, null),
    ('8fb21820-05a0-4c32-8b1d-96ade5f586d4',  null, 'Example 2', 'user2@example.com', '11987654321', 'Sim', 'Não', 12, 'Não tem', 'REGULAR', 1, 172, 68.5, false, '56e88ae8-6a11-468a-a6fb-08cd2ac07a0b', true, false, true, null, null),
    ('ba7a3453-3edd-46c4-b471-050e58fd1e28',  null, 'Example 3', 'user3@example.com', '11876543210', 'Não tem', 'Sim', 24, 'Sim', 'EM_ALERTA', 0, 160, 62.0, false, 'fe2baa3a-59fc-43c7-a5b2-8e3e872471d7', true, false, true, null, null),
    ('67a04b94-ddd2-4a60-8d10-0a8d03861f33',  null, 'Example 4', 'user4@example.com', '11765432109', 'Não tem', 'Não', 8, 'Sim', 'EM_ALERTA', 0, 178, 85.3, false, '2f2866be-4738-4602-b1ea-27da45143b6c', false, true, true, null, null),
    ('ab97a243-3717-464a-a6f4-5547966686b8',  null, 'Example 5', 'user5@example.com', '11654321098', 'Sim', 'Não', 15, 'Não tem', 'REGULAR', 0, 165, 70.8, false, '1bfac9c7-4652-48bb-b4b7-46983225831b', false, true, true, null, null),
    ('49803705-f05b-47b2-b3f8-7eaf1a98458c',  null, 'Example 6', 'user6@example.com', '11543210987', 'Não tem', 'Sim', 6, 'Não tem', 'REGULAR', 3, 182, 90.5, false, '3e291362-6377-43a6-a3a7-bf73918f5120', false, true, true, null, null),
    ('7c18169a-b57f-4047-80d7-c1fb9d1df25b',  null, 'Example 7', 'user7@example.com', '11432109876', 'Sim', 'Não', 10, 'Não tem', 'REGULAR', 0, 170, 65.2, false, 'd47d73f0-70db-4cb1-aa1b-243d363b476b', true, false, true, null, null),
    ('d2bc643e-bb7f-48a7-bec8-cad6393a74dd',  null, 'Example 8', 'user8@example.com', '11321098765', 'Não tem', 'Sim', 20, 'Sim', 'EM_ALERTA', 3, 175, 77.6, false, 'a780503d-88ff-4541-903f-f033f45f42c2', true, false, true, null, null),
    ('d5d7f70a-85c7-4edb-8c96-f2e019121690',  null, 'Example 9', 'user9@example.com', '11210987654', 'Sim', 'Não', 18, 'Não tem', 'REGULAR', 0, 168, 72.4, false, 'd47d73f0-70db-4cb1-aa1b-243d363b476b', false, false, true, null, null),
    ('7ee65990-0033-4767-b372-217557f6b5f4',  null, 'Example 10', 'user10@example.com', '11109876543', 'Não tem', 'Sim', 7, 'Sim', 'REGULAR', 0, 180, 80.1, false, 'd47d73f0-70db-4cb1-aa1b-243d363b476b', false, false, true, null, null),
    ('cfe9ac21-473a-4046-bf79-d627b222f200',  null, 'Example 31', 'user31@example.com','84979615895', 'Não tem', 'Sim', 5, 'Não tem', 'REGULAR', 1, 155, 54.2, false, 'dc93003f-59b9-4b4c-b8dd-9681a7c6d8a8', false, false, true, null, null),
    ('9df33993-7e5e-470c-b14b-cd4c7463faeb',  null, 'Example 32', 'user32@example.com', '1198835421', 'Sim', 'Não', 12, 'Não tem', 'REGULAR', 2, 172, 68.5, false, '0f0fd5be-b0c2-4726-953a-68fe80ffc434', false, false, false, null, null),
    ('e48a756d-6aea-4916-b1d5-837a36d3186f',  null, 'Example 33', 'user33@example.com', '01927543210', 'Não tem', 'Sim', 24, 'Sim',  'EM_ALERTA', 0, 160, 62.0, false, '6f160df3-f6d0-4e96-99fd-36c4b0ad23cd', true, false, false, null, null),
    ('bb397bf5-f0c7-437b-9ed0-8342fac27870',  null, 'Example 34', 'user34@example.com', '88109274536', 'Não tem', 'Não', 8, 'Sim',  'EM_ALERTA', 0, 178, 85.3, false, '4efea951-827d-4736-b399-668525227873', true, false, false, null, null),
    ('eb718e0f-3404-4708-800a-e02873e22dd0',  null, 'Example 35', 'user35@example.com', '88992837162', 'Sim', 'Não', 15, 'Não tem', 'REGULAR', 0, 165, 70.8, false, 'a780503d-88ff-4541-903f-f033f45f42c2', true, false, false, null, null),
    ('10f97bb1-5155-40e6-992e-1605b814843f',  null, 'Example 36', 'user36@example.com', '88110462729', 'Não tem', 'Sim', 6, 'Não tem', 'REGULAR', 1, 182, 90.5, false, '3e291362-6377-43a6-a3a7-bf73918f5120', false, true, false, null, null),
    ('8d4017cb-b9a2-4324-976c-27acfeb0c2ad',  null, 'Example 37', 'user37@example.com', '88471039654', 'Sim', 'Não', 10, 'Não tem', 'REGULAR', 2, 170, 65.2, false, 'd47d73f0-70db-4cb1-aa1b-243d363b476b', false, true, false, null, null),
    ('c6d7625a-d669-47e9-af37-d44f11614fc9',  null, 'Example 38', 'user38@example.com', '21847362930', 'Não tem', 'Sim', 20, 'Sim',  'EM_ALERTA', 3, 175, 77.6, false, 'a780503d-88ff-4541-903f-f033f45f42c2', false, true, false, null, null),
    ('6dadb572-1b0e-4e40-9915-3c4feafac8e0',  null, 'Example 39', 'user39@example.com', '33210987654', 'Sim', 'Não', 18, 'Não tem', 'REGULAR', 0, 168, 72.4, false, 'd47d73f0-70db-4cb1-aa1b-243d363b476b', false, false, false, null, null),
    ('68dbf89a-b0f3-4b94-ad94-4d015e8d095b',  null, 'Example 40', 'user40@example.com', '22109876543', 'Não tem', 'Sim', 7, 'Sim', 'REGULAR', 0, 180, 80.1, false, 'd47d73f0-70db-4cb1-aa1b-243d363b476b', false, false, false, null, null),
    ('0b3bec3f-5f1c-4a2e-a243-177139138c03',  1, 'Example 11', 'user11@example.com', '98982267175', null, null, null, null, null, null, null, null, true, null, null, null, null,  'b4e876c4-a977-442f-a746-7317de9d70e2', ARRAY['MANHA', 'TARDE']),
    ('249c514f-26de-4955-881c-d4077f0db4ad',  2, 'Example 12', 'user12@example.com', '46998796251', null, null, null, null, null, null, null, null, true, null, null, null, null, 'b4e876c4-a977-442f-a746-7317de9d70e2', ARRAY['NOITE', 'MANHA']),
    ('d27c532d-b14c-4285-ab21-3ce2001ecad6',  3, 'Example 13', 'user13@example.com', '79986994933', null, null, null, null, null, null, null, null, true, null, null, null, null,  '7098316a-87f0-457f-936b-79e5d538b69d', ARRAY['NOITE, TARDE']),
    ('18b044a0-2a52-4cc6-b3be-0ff6ddf00d0f',  4, 'Example 14', 'user14@example.com', '45966727571', null, null, null, null, null, null, null, null, true, null, null, null, null,  '7098316a-87f0-457f-936b-79e5d538b69d', ARRAY['TARDE']),
    ('a0ede6e2-6749-4e8d-9fbc-a3910f93ea4d',  5, 'Example 15', 'user15@example.com', '32977579541', null, null, null, null, null, null, null, null, true, null, null, null, null,  'd9b9f322-4c2d-40e7-aee8-a46048c49c1d', ARRAY['MANHA']),
    ('f71bd358-8270-46f9-b9c7-9413dfca3137',  6, 'Example 16', 'user16@example.com', '94963369420', null, null, null, null, null, null, null, null, true, null, null, null, null,  'd9b9f322-4c2d-40e7-aee8-a46048c49c1d', ARRAY['MANHA', 'NOITE']),
    ('251e223c-ed70-4ad1-82cd-6e4b2315af7a',  7, 'Example 17', 'user17@example.com', '14985843910', null, null, null, null, null, null, null, null, true, null, null, null, null,  '8307c2da-aeb7-4f25-94a0-edcf820b0e59', ARRAY['TARDE', 'MANHA']),
    ('24537481-8eab-46b8-af9a-cfcfd02ba029',  8, 'Example 18', 'user18@example.com', '48965004615', null, null, null, null, null, null, null, null, true, null, null, null, null, '8307c2da-aeb7-4f25-94a0-edcf820b0e59', ARRAY['MANHA', 'NOITE']),
    ('69db9901-e6fe-4b66-9127-f4b6b22583cf',  9, 'Example 19', 'user19@example.com', '92972334633', null, null, null, null, null, null, null, null, true, null, null, null, null, '8307c2da-aeb7-4f25-94a0-edcf820b0e59', null),
    ('ae57500f-b412-4a11-99f4-4ec87aa23611',  10, 'Example 20', 'user20@example.com', '16996887336', null, null, null, null, null, null, null, null, true, null, null, null, null, '8307c2da-aeb7-4f25-94a0-edcf820b0e59', null),
    ('a3f953fb-90a7-4d65-8d39-5f1c0916d111',  11, 'Example 21', 'user21@example.com', '68976038867', null, null, null, null, null, null, null, null, true, null, null, null, null,  '5af35073-ccf6-4986-b31f-53f1c29ac821', null),
    ('af3d470b-0ad7-4f13-a56a-8ff752b921ff',  12, 'Example 22', 'user22@example.com', '33975842867', null, null, null, null, null, null, null, null, true, null, null, null, null,  '5af35073-ccf6-4986-b31f-53f1c29ac821', null),
    ('9c5a9e02-60f8-4c9c-bdf1-e0c7a6e10341',  13, 'Example 23', 'user23@example.com', '43971371588', null, null, null, null, null, null, null, null, true, null, null, null, null,  '6f501097-2c63-4469-bf5f-7abe1d62d615', null),
    ('0a71e732-9d82-4c5b-97df-2c6e896749f4',  14, 'Example 24', 'user24@example.com', '69981637027', null, null, null, null, null, null, null, null, true, null, null, null, null,  '6f501097-2c63-4469-bf5f-7abe1d62d615', null),
    ('cd69df5f-ec3a-4c1b-bc58-9b1b065e9f1a',  15, 'Example 25', 'user25@example.com', '13975319876', null, null, null, null, null, null, null, null, true, null, null, null, null,  'e2389532-f419-4243-9d0a-edd0245845cd', null),
    ('3b4f4f65-7287-4d7b-bf22-1d645b909843',  16, 'Example 26', 'user26@example.com', '86983947518', null, null, null, null, null, null, null, null, true, null, null, null, null,  'e2389532-f419-4243-9d0a-edd0245845cd', null),
    ('1c2f6d5b-dc53-482e-b1d4-64c8265c0831',  17, 'Example 27', 'user27@example.com', '83964555149', null, null, null, null, null, null, null, null, true, null, null, null, null, null, ARRAY['NOITE']),
    ('84f4a34b-8c35-451f-a49c-f88e11844e28',  18, 'Example 28', 'user28@example.com', '96999531448', null, null, null, null, null, null, null, null, true, null, null, null, null, null, ARRAY['TARDE']),
    ('ae645ff3-5b9d-4898-8a18-f578bdc31634',  19, 'Example 29', 'user29@example.com', '42978805855', null, null, null, null, null, null, null, null, true, null, null, null, null, null, null),
    ('ebc4f5fc-29c3-4c26-a18c-49df9a3f4a53',  20, 'Example 30', 'user30@example.com', '65992323600', null, null, null, null, null, null, null, null, true, null, null, null, null, null, null);

INSERT INTO tb_presenca(id, aluno_id, usuario_id, data, presente) VALUES
  (gen_random_uuid(), '00d67ac7-a1e9-4abd-a393-8464b77c0c9e', 'f1d13d02-9ebe-4f38-b372-faac79e82991', '10/28/2024', true),
  (gen_random_uuid(), '00d67ac7-a1e9-4abd-a393-8464b77c0c9e', 'f1d13d02-9ebe-4f38-b372-faac79e82991', '10/29/2024', true),
  (gen_random_uuid(), '00d67ac7-a1e9-4abd-a393-8464b77c0c9e', 'f1d13d02-9ebe-4f38-b372-faac79e82991', '10/30/2024', true),
  (gen_random_uuid(), '00d67ac7-a1e9-4abd-a393-8464b77c0c9e', 'f1d13d02-9ebe-4f38-b372-faac79e82991', '10/31/2024', false),
  (gen_random_uuid(), '00d67ac7-a1e9-4abd-a393-8464b77c0c9e', 'f1d13d02-9ebe-4f38-b372-faac79e82991', '11/01/2024', false),
  (gen_random_uuid(), '8fb21820-05a0-4c32-8b1d-96ade5f586d4', 'f1d13d02-9ebe-4f38-b372-faac79e82991', '10/28/2024', true),
  (gen_random_uuid(), '8fb21820-05a0-4c32-8b1d-96ade5f586d4', 'f1d13d02-9ebe-4f38-b372-faac79e82991', '10/29/2024', true),
  (gen_random_uuid(), '8fb21820-05a0-4c32-8b1d-96ade5f586d4', 'f1d13d02-9ebe-4f38-b372-faac79e82991', '10/30/2024', true),
  (gen_random_uuid(), '8fb21820-05a0-4c32-8b1d-96ade5f586d4', 'f1d13d02-9ebe-4f38-b372-faac79e82991', '10/31/2024', true),
  (gen_random_uuid(), '8fb21820-05a0-4c32-8b1d-96ade5f586d4', 'f1d13d02-9ebe-4f38-b372-faac79e82991', '11/01/2024', false),
  (gen_random_uuid(), 'ba7a3453-3edd-46c4-b471-050e58fd1e28', 'f1d13d02-9ebe-4f38-b372-faac79e82991', '10/28/2024', true),
  (gen_random_uuid(), 'ba7a3453-3edd-46c4-b471-050e58fd1e28', 'f1d13d02-9ebe-4f38-b372-faac79e82991', '10/29/2024', false),
  (gen_random_uuid(), 'ba7a3453-3edd-46c4-b471-050e58fd1e28', 'f1d13d02-9ebe-4f38-b372-faac79e82991', '10/30/2024', false),
  (gen_random_uuid(), 'ba7a3453-3edd-46c4-b471-050e58fd1e28', 'f1d13d02-9ebe-4f38-b372-faac79e82991', '10/31/2024', true),
  (gen_random_uuid(), 'ba7a3453-3edd-46c4-b471-050e58fd1e28', 'f1d13d02-9ebe-4f38-b372-faac79e82991', '11/01/2024', true),
  (gen_random_uuid(), '67a04b94-ddd2-4a60-8d10-0a8d03861f33', 'f1d13d02-9ebe-4f38-b372-faac79e82991', '10/28/2024', true),
  (gen_random_uuid(), '67a04b94-ddd2-4a60-8d10-0a8d03861f33', 'f1d13d02-9ebe-4f38-b372-faac79e82991', '10/29/2024', true),
  (gen_random_uuid(), '67a04b94-ddd2-4a60-8d10-0a8d03861f33', 'f1d13d02-9ebe-4f38-b372-faac79e82991', '10/30/2024', true),
  (gen_random_uuid(), '67a04b94-ddd2-4a60-8d10-0a8d03861f33', 'f1d13d02-9ebe-4f38-b372-faac79e82991', '10/31/2024', true),
  (gen_random_uuid(), '67a04b94-ddd2-4a60-8d10-0a8d03861f33', 'f1d13d02-9ebe-4f38-b372-faac79e82991', '11/01/2024', true),
  (gen_random_uuid(), 'ab97a243-3717-464a-a6f4-5547966686b8', 'f1d13d02-9ebe-4f38-b372-faac79e82991', '10/28/2024', true),
  (gen_random_uuid(), 'ab97a243-3717-464a-a6f4-5547966686b8', 'f1d13d02-9ebe-4f38-b372-faac79e82991', '10/29/2024', true),
  (gen_random_uuid(), 'ab97a243-3717-464a-a6f4-5547966686b8', 'f1d13d02-9ebe-4f38-b372-faac79e82991', '10/30/2024', false),
  (gen_random_uuid(), 'ab97a243-3717-464a-a6f4-5547966686b8', 'f1d13d02-9ebe-4f38-b372-faac79e82991', '10/31/2024', false),
  (gen_random_uuid(), 'ab97a243-3717-464a-a6f4-5547966686b8', 'f1d13d02-9ebe-4f38-b372-faac79e82991', '11/01/2024', true),
  (gen_random_uuid(), '49803705-f05b-47b2-b3f8-7eaf1a98458c', 'f1d13d02-9ebe-4f38-b372-faac79e82991', '10/28/2024', true),
  (gen_random_uuid(), '49803705-f05b-47b2-b3f8-7eaf1a98458c', 'f1d13d02-9ebe-4f38-b372-faac79e82991', '10/29/2024', true),
  (gen_random_uuid(), '49803705-f05b-47b2-b3f8-7eaf1a98458c', 'f1d13d02-9ebe-4f38-b372-faac79e82991', '10/30/2024', false),
  (gen_random_uuid(), '49803705-f05b-47b2-b3f8-7eaf1a98458c', 'f1d13d02-9ebe-4f38-b372-faac79e82991', '10/31/2024', false),
  (gen_random_uuid(), '49803705-f05b-47b2-b3f8-7eaf1a98458c', 'f1d13d02-9ebe-4f38-b372-faac79e82991', '11/01/2024', false),
  (gen_random_uuid(), '7c18169a-b57f-4047-80d7-c1fb9d1df25b', 'f1d13d02-9ebe-4f38-b372-faac79e82991', '10/28/2024', true),
  (gen_random_uuid(), '7c18169a-b57f-4047-80d7-c1fb9d1df25b', 'f1d13d02-9ebe-4f38-b372-faac79e82991', '10/29/2024', true),
  (gen_random_uuid(), '7c18169a-b57f-4047-80d7-c1fb9d1df25b', 'f1d13d02-9ebe-4f38-b372-faac79e82991', '10/30/2024', true),
  (gen_random_uuid(), '7c18169a-b57f-4047-80d7-c1fb9d1df25b', 'f1d13d02-9ebe-4f38-b372-faac79e82991', '10/31/2024', true),
  (gen_random_uuid(), '7c18169a-b57f-4047-80d7-c1fb9d1df25b', 'f1d13d02-9ebe-4f38-b372-faac79e82991', '11/01/2024', true),

  (gen_random_uuid(), 'd2bc643e-bb7f-48a7-bec8-cad6393a74dd', '75b8e85b-2c70-4352-9903-305918c34f34', '10/28/2024', true),
  (gen_random_uuid(), 'd2bc643e-bb7f-48a7-bec8-cad6393a74dd', '75b8e85b-2c70-4352-9903-305918c34f34', '10/29/2024', true),
  (gen_random_uuid(), 'd2bc643e-bb7f-48a7-bec8-cad6393a74dd', '75b8e85b-2c70-4352-9903-305918c34f34', '10/30/2024', true),
  (gen_random_uuid(), 'd2bc643e-bb7f-48a7-bec8-cad6393a74dd', '75b8e85b-2c70-4352-9903-305918c34f34', '10/31/2024', false),
  (gen_random_uuid(), 'd2bc643e-bb7f-48a7-bec8-cad6393a74dd', '75b8e85b-2c70-4352-9903-305918c34f34', '11/01/2024', false),
  (gen_random_uuid(), 'd5d7f70a-85c7-4edb-8c96-f2e019121690', '75b8e85b-2c70-4352-9903-305918c34f34', '10/28/2024', true),
  (gen_random_uuid(), 'd5d7f70a-85c7-4edb-8c96-f2e019121690', '75b8e85b-2c70-4352-9903-305918c34f34', '10/29/2024', true),
  (gen_random_uuid(), 'd5d7f70a-85c7-4edb-8c96-f2e019121690', '75b8e85b-2c70-4352-9903-305918c34f34', '10/30/2024', true),
  (gen_random_uuid(), 'd5d7f70a-85c7-4edb-8c96-f2e019121690', '75b8e85b-2c70-4352-9903-305918c34f34', '10/31/2024', true),
  (gen_random_uuid(), 'd5d7f70a-85c7-4edb-8c96-f2e019121690', '75b8e85b-2c70-4352-9903-305918c34f34', '11/01/2024', false),
  (gen_random_uuid(), 'ba7a3453-3edd-46c4-b471-050e58fd1e28', '75b8e85b-2c70-4352-9903-305918c34f34', '10/28/2024', true),
  (gen_random_uuid(), 'ba7a3453-3edd-46c4-b471-050e58fd1e28', '75b8e85b-2c70-4352-9903-305918c34f34', '10/29/2024', false),
  (gen_random_uuid(), 'ba7a3453-3edd-46c4-b471-050e58fd1e28', '75b8e85b-2c70-4352-9903-305918c34f34', '10/30/2024', false),
  (gen_random_uuid(), 'ba7a3453-3edd-46c4-b471-050e58fd1e28', '75b8e85b-2c70-4352-9903-305918c34f34', '10/31/2024', true),
  (gen_random_uuid(), 'ba7a3453-3edd-46c4-b471-050e58fd1e28', '75b8e85b-2c70-4352-9903-305918c34f34', '11/01/2024', true),
  (gen_random_uuid(), 'cfe9ac21-473a-4046-bf79-d627b222f200', '75b8e85b-2c70-4352-9903-305918c34f34', '10/28/2024', true),
  (gen_random_uuid(), 'cfe9ac21-473a-4046-bf79-d627b222f200', '75b8e85b-2c70-4352-9903-305918c34f34', '10/29/2024', true),
  (gen_random_uuid(), 'cfe9ac21-473a-4046-bf79-d627b222f200', '75b8e85b-2c70-4352-9903-305918c34f34', '10/30/2024', true),
  (gen_random_uuid(), 'cfe9ac21-473a-4046-bf79-d627b222f200', '75b8e85b-2c70-4352-9903-305918c34f34', '10/31/2024', true),
  (gen_random_uuid(), 'cfe9ac21-473a-4046-bf79-d627b222f200', '75b8e85b-2c70-4352-9903-305918c34f34', '11/01/2024', true),
  (gen_random_uuid(), '9df33993-7e5e-470c-b14b-cd4c7463faeb', '75b8e85b-2c70-4352-9903-305918c34f34', '10/28/2024', true),
  (gen_random_uuid(), '9df33993-7e5e-470c-b14b-cd4c7463faeb', '75b8e85b-2c70-4352-9903-305918c34f34', '10/29/2024', true),
  (gen_random_uuid(), '9df33993-7e5e-470c-b14b-cd4c7463faeb', '75b8e85b-2c70-4352-9903-305918c34f34', '10/30/2024', false),
  (gen_random_uuid(), '9df33993-7e5e-470c-b14b-cd4c7463faeb', '75b8e85b-2c70-4352-9903-305918c34f34', '10/31/2024', false),
  (gen_random_uuid(), 'e48a756d-6aea-4916-b1d5-837a36d3186f', '75b8e85b-2c70-4352-9903-305918c34f34', '10/28/2024', true),
  (gen_random_uuid(), 'e48a756d-6aea-4916-b1d5-837a36d3186f', '75b8e85b-2c70-4352-9903-305918c34f34', '10/29/2024', false),
  (gen_random_uuid(), 'e48a756d-6aea-4916-b1d5-837a36d3186f', '75b8e85b-2c70-4352-9903-305918c34f34', '10/30/2024', false),
  (gen_random_uuid(), 'e48a756d-6aea-4916-b1d5-837a36d3186f', '75b8e85b-2c70-4352-9903-305918c34f34', '10/31/2024', false),
  (gen_random_uuid(), 'e48a756d-6aea-4916-b1d5-837a36d3186f', '75b8e85b-2c70-4352-9903-305918c34f34', '11/01/2024', false),
  (gen_random_uuid(), 'bb397bf5-f0c7-437b-9ed0-8342fac27870', '75b8e85b-2c70-4352-9903-305918c34f34', '10/28/2024', true),
  (gen_random_uuid(), 'bb397bf5-f0c7-437b-9ed0-8342fac27870', '75b8e85b-2c70-4352-9903-305918c34f34', '10/29/2024', true),
  (gen_random_uuid(), 'bb397bf5-f0c7-437b-9ed0-8342fac27870', '75b8e85b-2c70-4352-9903-305918c34f34', '10/30/2024', true),
  (gen_random_uuid(), 'bb397bf5-f0c7-437b-9ed0-8342fac27870', '75b8e85b-2c70-4352-9903-305918c34f34', '10/31/2024', true),
  (gen_random_uuid(), 'bb397bf5-f0c7-437b-9ed0-8342fac27870', '75b8e85b-2c70-4352-9903-305918c34f34', '11/01/2024', true),

  (gen_random_uuid(), 'eb718e0f-3404-4708-800a-e02873e22dd0', '915b6311-2965-4e34-aa82-8f89a1ea4c38', '10/28/2024', true),
  (gen_random_uuid(), 'eb718e0f-3404-4708-800a-e02873e22dd0', '915b6311-2965-4e34-aa82-8f89a1ea4c38', '10/29/2024', true),
  (gen_random_uuid(), 'eb718e0f-3404-4708-800a-e02873e22dd0', '915b6311-2965-4e34-aa82-8f89a1ea4c38', '10/30/2024', true),
  (gen_random_uuid(), 'eb718e0f-3404-4708-800a-e02873e22dd0', '915b6311-2965-4e34-aa82-8f89a1ea4c38', '10/31/2024', false),
  (gen_random_uuid(), 'eb718e0f-3404-4708-800a-e02873e22dd0', '915b6311-2965-4e34-aa82-8f89a1ea4c38', '11/01/2024', false),
  (gen_random_uuid(), '10f97bb1-5155-40e6-992e-1605b814843f', '915b6311-2965-4e34-aa82-8f89a1ea4c38', '10/28/2024', true),
  (gen_random_uuid(), '10f97bb1-5155-40e6-992e-1605b814843f', '915b6311-2965-4e34-aa82-8f89a1ea4c38', '10/29/2024', true),
  (gen_random_uuid(), '10f97bb1-5155-40e6-992e-1605b814843f', '915b6311-2965-4e34-aa82-8f89a1ea4c38', '10/30/2024', true),
  (gen_random_uuid(), '10f97bb1-5155-40e6-992e-1605b814843f', '915b6311-2965-4e34-aa82-8f89a1ea4c38', '10/31/2024', true),
  (gen_random_uuid(), '10f97bb1-5155-40e6-992e-1605b814843f', '915b6311-2965-4e34-aa82-8f89a1ea4c38', '11/01/2024', false),
  (gen_random_uuid(), '8d4017cb-b9a2-4324-976c-27acfeb0c2ad', '915b6311-2965-4e34-aa82-8f89a1ea4c38', '10/28/2024', true),
  (gen_random_uuid(), '8d4017cb-b9a2-4324-976c-27acfeb0c2ad', '915b6311-2965-4e34-aa82-8f89a1ea4c38', '10/29/2024', false),
  (gen_random_uuid(), '8d4017cb-b9a2-4324-976c-27acfeb0c2ad', '915b6311-2965-4e34-aa82-8f89a1ea4c38', '10/30/2024', false),
  (gen_random_uuid(), '8d4017cb-b9a2-4324-976c-27acfeb0c2ad', '915b6311-2965-4e34-aa82-8f89a1ea4c38', '10/31/2024', true),
  (gen_random_uuid(), '8d4017cb-b9a2-4324-976c-27acfeb0c2ad', '915b6311-2965-4e34-aa82-8f89a1ea4c38', '11/01/2024', true),
  (gen_random_uuid(), 'c6d7625a-d669-47e9-af37-d44f11614fc9', '915b6311-2965-4e34-aa82-8f89a1ea4c38', '10/28/2024', true),
  (gen_random_uuid(), 'c6d7625a-d669-47e9-af37-d44f11614fc9', '915b6311-2965-4e34-aa82-8f89a1ea4c38', '10/29/2024', true),
  (gen_random_uuid(), 'c6d7625a-d669-47e9-af37-d44f11614fc9', '915b6311-2965-4e34-aa82-8f89a1ea4c38', '10/30/2024', true),
  (gen_random_uuid(), 'c6d7625a-d669-47e9-af37-d44f11614fc9', '915b6311-2965-4e34-aa82-8f89a1ea4c38', '10/31/2024', false),
  (gen_random_uuid(), 'c6d7625a-d669-47e9-af37-d44f11614fc9', '915b6311-2965-4e34-aa82-8f89a1ea4c38', '11/01/2024', false),
  (gen_random_uuid(), '6dadb572-1b0e-4e40-9915-3c4feafac8e0', '915b6311-2965-4e34-aa82-8f89a1ea4c38', '10/28/2024', false),
  (gen_random_uuid(), '6dadb572-1b0e-4e40-9915-3c4feafac8e0', '915b6311-2965-4e34-aa82-8f89a1ea4c38', '10/29/2024', true),
  (gen_random_uuid(), '6dadb572-1b0e-4e40-9915-3c4feafac8e0', '915b6311-2965-4e34-aa82-8f89a1ea4c38', '10/30/2024', false),
  (gen_random_uuid(), '6dadb572-1b0e-4e40-9915-3c4feafac8e0', '915b6311-2965-4e34-aa82-8f89a1ea4c38', '10/31/2024', false),
  (gen_random_uuid(), '6dadb572-1b0e-4e40-9915-3c4feafac8e0', '915b6311-2965-4e34-aa82-8f89a1ea4c38', '11/01/2024', true),
  (gen_random_uuid(), '68dbf89a-b0f3-4b94-ad94-4d015e8d095b', '915b6311-2965-4e34-aa82-8f89a1ea4c38', '10/28/2024', true),
  (gen_random_uuid(), '68dbf89a-b0f3-4b94-ad94-4d015e8d095b', '915b6311-2965-4e34-aa82-8f89a1ea4c38', '10/29/2024', true),
  (gen_random_uuid(), '68dbf89a-b0f3-4b94-ad94-4d015e8d095b', '915b6311-2965-4e34-aa82-8f89a1ea4c38', '10/30/2024', false),
  (gen_random_uuid(), '68dbf89a-b0f3-4b94-ad94-4d015e8d095b', '915b6311-2965-4e34-aa82-8f89a1ea4c38', '10/31/2024', false),
  (gen_random_uuid(), '68dbf89a-b0f3-4b94-ad94-4d015e8d095b', '915b6311-2965-4e34-aa82-8f89a1ea4c38', '11/01/2024', false);

SET session_replication_role = 'origin';
