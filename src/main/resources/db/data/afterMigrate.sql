
DELETE FROM aluno;

INSERT INTO aluno(id, turno_id, colocacao, nome, email, telefone, cirurgias, patologias, meses_experiencia_musculacao, diagnostico_lesao_joelho, status, ausencias_consecutivas, altura, peso, lista_espera) VALUES
    ('28a6794b-96b8-48ef-9fe2-899b44a4ae35', null, null, 'Example 1', 'user1@example.com','1140028922', 'Não tem', 'Sim', 5, 'Não tem', 0, 1, 155, 54.2, false),
    ('2b74a4c9-233f-4b0b-845b-3d4f75b32b45', null, null, 'Example 2', 'user2@example.com', '11987654321', 'Sim', 'Não', 12, 'Não tem', 0, 2, 172, 68.5, false),
    ('3c85b2dc-987a-4f68-91c1-6b8a63947df3', null, null, 'Example 3', 'user3@example.com', '11876543210', 'Não tem', 'Sim', 24, 'Sim', 1, 4, 160, 62.0, false),
    ('4d96e3ed-1f23-4f7d-b9c7-d8e647b62c5a', null, null, 'Example 4', 'user4@example.com', '11765432109', 'Não tem', 'Não', 8, 'Sim', 1, 2, 178, 85.3, false),
    ('5e07f4fe-34b2-4d4a-8ca4-7b9a74683fa6', null, null, 'Example 5', 'user5@example.com', '11654321098', 'Sim', 'Não', 15, 'Não tem', 0, 0, 165, 70.8, false),
    ('6f18a5ff-45c3-4e5b-9da3-8c9b84794df7', null, null, 'Example 6', 'user6@example.com', '11543210987', 'Não tem', 'Sim', 6, 'Não tem', 0, 1, 182, 90.5, false),
    ('ddc7b758-93cf-424c-81d9-2d301fd55677', null, null, 'Example 7', 'user7@example.com', '11432109876', 'Sim', 'Não', 10, 'Não tem', 0, 2, 170, 65.2, false),
    ('c461e424-d59a-470e-91fc-3f095035362d', null, null, 'Example 8', 'user8@example.com', '11321098765', 'Não tem', 'Sim', 20, 'Sim', 1, 3, 175, 77.6, false),
    ('d47461a5-c08c-41dc-914c-f027dcbb783e', null, null, 'Example 9', 'user9@example.com', '11210987654', 'Sim', 'Não', 18, 'Não tem', 0, 0, 168, 72.4, false),
    ('b97eec73-29c3-45eb-b91d-e5531ef4d947', null, null, 'Example 10', 'user10@example.com', '11109876543', 'Não tem', 'Sim', 7, 'Sim', 0, 0, 180, 80.1, false),
    ('f0d04bb5-05e6-46af-8ad0-dcb3b931e834', null, 1, 'Example 11', 'user11@example.com', null, null, null, null, null, null, null, null, null, true),
    ('5b484b9a-aca1-4b21-aff2-6b8de8fadc22', null, 2, 'Example 12', 'user12@example.com', null, null, null, null, null, null, null, null, null, true),
    ('0b77d4cf-af76-46dd-bb4e-466c11d2dd71', null, 3, 'Example 13', 'user13@example.com', null, null, null, null, null, null, null, null, null, true),
    ('302b7b7e-3e9f-472b-917d-8240b965e8ef', null, 4, 'Example 14', 'user14@example.com', null, null, null, null, null, null, null, null, null, true),
    ('176f2da0-0268-497c-8a88-ff34aff5ba73', null, 5, 'Example 15', 'user15@example.com', null, null, null, null, null, null, null, null, null, true),
    ('fe24e75b-0974-4c95-afac-4540cdde3e6b', null, 6, 'Example 16', 'user16@example.com', null, null, null, null, null, null, null, null, null, true),
    ('55e7f3b7-837a-4a00-be62-e3351e20b5fe', null, 7, 'Example 17', 'user17@example.com', null, null, null, null, null, null, null, null, null, true),
    ('6304b250-12e0-4582-bdbe-28b1fc50ee65', null, 8, 'Example 18', 'user18@example.com', null, null, null, null, null, null, null, null, null, true),
    ('e7daec0d-0f66-41ae-8a43-e0705c316e93', null, 9, 'Example 19', 'user19@example.com', null, null, null, null, null, null, null, null, null, true),
    ('efc4ce69-d4ad-4112-ba89-8c7c93f9d5d4', null, 10, 'Example 20', 'user20@example.com', null, null, null, null, null, null, null, null, null, true);