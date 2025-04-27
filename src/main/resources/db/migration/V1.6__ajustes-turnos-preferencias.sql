drop table tb_aluno_horario_preferencia cascade;
drop table tb_aluno_interessado cascade;
drop table tb_horario_interesse cascade;

alter table tb_horario drop column horario_interesse_id;
alter table tb_horario drop column turno;
alter table tb_aluno drop column aluno_interessado_id;
alter table tb_aluno drop column turnos_preferenciais;

create table tb_turno (
    id uuid primary key,
    nome varchar
);

alter table tb_horario add column turno_id uuid;
alter table tb_horario add constraint fk_horario_turno foreign key (turno_id) references tb_turno(id);

create table tb_turnos_preferenciais (
    aluno_id uuid,
    turno_id uuid,

    primary key (aluno_id, turno_id),

    foreign key (aluno_id) references tb_aluno(id),
    foreign key (turno_id) references tb_turno(id)
);