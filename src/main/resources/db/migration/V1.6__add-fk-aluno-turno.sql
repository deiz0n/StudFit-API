ALTER TABLE turno ADD COLUMN aluno_id uuid;

ALTER TABLE turno ADD CONSTRAINT fk_aluno_turno FOREIGN KEY (aluno_id) REFERENCES aluno(id)