create table if not exists tb_aluno_horario_preferencia
(
    id uuid not null,
    constraint tb_aluno_horario_preferencia_pkey
        primary key (id)
);

create table if not exists tb_aluno_interessado
(
    id uuid not null,
    aluno_horario_preferencia_id uuid,
    constraint tb_aluno_interessado_pkey
        primary key (id),
    constraint fkr7nq5cw9u998r8rfw9hu9aewx
        foreign key (aluno_horario_preferencia_id) references tb_aluno_horario_preferencia
);

create table if not exists tb_aluno
(
    id uuid not null,
    altura integer,
    atestado bytea,
    ausencias_consecutivas integer,
    cirurgias varchar(255),
    colocacao integer,
    consumo_alcool boolean,
    consumo_cigarro boolean,
    diagnostico_lesao_joelho varchar(255),
    email varchar(50),
    lista_espera boolean,
    meses_experiencia_musculacao integer,
    nome varchar(150),
    patologias varchar(255),
    peso double precision,
    pratica_exercicio_fisico boolean,
    status varchar(255),
    telefone varchar(11),
    aluno_interessado_id uuid,
    constraint tb_aluno_pkey
        primary key (id),
    constraint uk83vpjsn4ih6eij9prsq3pqn3w
        unique (email),
    constraint uko6n7w4uka40mvq10bx1mflo75
        unique (telefone),
    constraint fk34bvjy36k9idke30vk1hxdgsm
        foreign key (aluno_interessado_id) references tb_aluno_interessado,
    constraint tb_aluno_status_check
        check ((status)::text = ANY ((ARRAY['REGULAR'::character varying, 'EM_ALERTA'::character varying])::text[]))
);

create table if not exists tb_horario_interesse
(
    id uuid not null,
    aluno_horario_preferencia_id uuid,
    constraint tb_horario_interesse_pkey
        primary key (id),
    constraint fks43od3ue0exih21we7f1cibxs
        foreign key (aluno_horario_preferencia_id) references tb_aluno_horario_preferencia
);

create table if not exists tb_horario
(
    id uuid not null,
    horario_final time(6),
    horario_inicial time(6),
    turno varchar(255),
    vagas_disponiveis integer,
    horario_interesse_id uuid,
    constraint tb_horario_pkey
        primary key (id),
    constraint fkfiqbd3blmmxn3yivrs5elimbi
        foreign key (horario_interesse_id) references tb_horario_interesse,
    constraint tb_horario_turno_check
        check ((turno)::text = ANY ((ARRAY['MANHA'::character varying, 'TARDE'::character varying, 'NOITE'::character varying])::text[]))
);

create table if not exists tb_presenca
(
    id uuid not null,
    data date,
    presente boolean,
    aluno_id uuid,
    usuario_id uuid,
    constraint tb_presenca_pkey
        primary key (id),
    constraint ukif5p38k5hmp97otwhvliktp37
        unique (usuario_id),
    constraint fkqcfdv5nnl6u9uk4wcxrpjd2e2
        foreign key (aluno_id) references tb_aluno
);

create table if not exists tb_usuario
(
    id uuid not null,
    cargo varchar(255),
    codigo_recuperacao varchar(6),
    email varchar(50),
    nome varchar(150),
    senha varchar(255),
    presenca_id uuid,
    constraint tb_usuario_pkey
        primary key (id),
    constraint ukspmnyb4dsul95fjmr5kmdmvub
        unique (email),
    constraint uk4nwq9b0a7puwch87j7cyxfuw5
        unique (presenca_id),
    constraint fkd8mwur1m65c4g2712u50vbayo
        foreign key (presenca_id) references tb_presenca,
    constraint tb_usuario_cargo_check
        check ((cargo)::text = ANY ((ARRAY['ADMINISTRADOR'::character varying, 'INSTRUTOR'::character varying, 'ESTAGIARIO'::character varying])::text[]))
);

alter table tb_presenca
    add constraint fkhg3vf386i9nuknwddm9knyu49
        foreign key (usuario_id) references tb_usuario;