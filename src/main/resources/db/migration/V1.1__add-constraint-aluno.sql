alter table tb_aluno add column horario_id uuid;

alter table tb_aluno add constraint fk_horario_aluno foreign key (horario_id) references tb_horario(id);