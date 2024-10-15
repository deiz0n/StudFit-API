ALTER TABlE aluno ADD COLUMN horario_id uuid;

ALTER TABLE aluno ADD CONSTRAINT fk_horario_aluno FOREIGN KEY (horario_id) REFERENCES horario(id)