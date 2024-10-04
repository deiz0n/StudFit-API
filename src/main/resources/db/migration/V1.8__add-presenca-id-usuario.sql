ALTER TABLE usuario ADD COLUMN presenca_id uuid;

ALTER TABLE usuariO ADD CONSTRAINT fk_presenca_usuario FOREIGN KEY (presenca_id) REFERENCES presenca(id)