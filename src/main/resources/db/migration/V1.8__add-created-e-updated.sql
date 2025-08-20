alter table tb_aluno
    add column created_at timestamp(6) default CURRENT_TIMESTAMP(6),
    add column updated_at timestamp(6) default CURRENT_TIMESTAMP(6);

alter table tb_horario
    add column created_at timestamp(6) default CURRENT_TIMESTAMP(6),
    add column updated_at timestamp(6) default CURRENT_TIMESTAMP(6);

alter table tb_presenca
    add column created_at timestamp(6) default CURRENT_TIMESTAMP(6),
    add column updated_at timestamp(6) default CURRENT_TIMESTAMP(6);

alter table tb_turno
    add column created_at timestamp(6) default CURRENT_TIMESTAMP(6),
    add column updated_at timestamp(6) default CURRENT_TIMESTAMP(6);

alter table tb_usuario
    add column created_at timestamp(6) default CURRENT_TIMESTAMP(6),
    add column updated_at timestamp(6) default CURRENT_TIMESTAMP(6);