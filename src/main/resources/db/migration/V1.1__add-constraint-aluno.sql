alter table tb_horario add column aluno_id uuid;

alter table tb_horario add constraint fk_aluno_horario foreign key (aluno_id) references tb_aluno(id);