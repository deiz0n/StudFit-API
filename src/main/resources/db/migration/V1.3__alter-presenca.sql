alter table tb_presenca drop column usuario_id;
alter table tb_presenca add column usuario_id uuid;
alter table tb_presenca add constraint fk_presenca_usuario foreign key (usuario_id) references tb_usuario(id);