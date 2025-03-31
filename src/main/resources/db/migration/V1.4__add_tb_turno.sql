create table if not exists tb_turno (
    id uuid not null,
    tipo_turno varchar,
    constraint tb_turno_pkey
        primary key (id),
    constraint
        tb_turno_tipo_turno_check
    check ((tipo_turno)::text = ANY ((ARRAY['MANHA'::character varying, 'TARDE'::character varying, 'NOITE'::character varying])::text[]))
);

alter table tb_horario
    add column turno_horario uuid;

alter table tb_horario
    add constraint fk_horario_turno
        foreign key (turno_horario)
            references tb_turno(id);