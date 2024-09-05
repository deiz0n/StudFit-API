CREATE TABLE IF NOT EXISTS aluno (
    id uuid NOT NULL PRIMARY KEY,
    turno_id uuid,
    colocacao serial NOT NULL,
    nome character varying(125) NOT NULL,
    email character varying(100) NOT NULL,
    telefone character varying(20) NOT NULL,
    cirurguas character varying(255),
    patologias character varying(255),
    meses_experiencias_musculacao character varying(255),
    diagnostico_lesao_joelho character varying(255),
    status character varying(15),
    ausencias_consecutivas integer,
    altura integer,
    peso double precision
);

CREATE TABLE IF NOT EXISTS presenca (
    id uuid NOT NULL PRIMARY KEY,
    aluno_id uuid NOT NULL,
    usuario_id uuid NOT NULL,
    data date NOT NULL,
    presente boolean NOT NULL
);

CREATE TABLE IF NOT EXISTS turno (
    id uuid NOT NULL PRIMARY KEY,
    horario_inicial timestamp without time zone NOT NULL,
    horario_final timestamp without time zone NOT NULL
);

CREATE TABLE IF NOT EXISTS usuario (
    id uuid NOT NULL PRIMARY KEY,
    nome character varying(125) NOT NULL,
    email character varying(100) NOT NULL,
    senha character varying(50) NOT NULL
);

ALTER TABLE aluno ADD CONSTRAINT fk_turno_aluno FOREIGN KEY (turno_id) REFERENCES turno(id);
ALTER TABLE presenca ADD CONSTRAINT fk_aluno_presenca FOREIGN KEY (aluno_id) REFERENCES aluno(id);
ALTER TABLE presenca ADD CONSTRAINT fk_usuario_presenca FOREIGN KEY (usuario_id) REFERENCES usuario(id);