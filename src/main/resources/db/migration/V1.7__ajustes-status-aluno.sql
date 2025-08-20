alter table tb_aluno drop constraint tb_aluno_status_check;

alter table tb_aluno
    add constraint tb_aluno_status_check
        check ((status)::text = ANY ((ARRAY['REGULAR'::character varying, 'EM_ALERTA'::character varying, 'CADASTRO_PENDENTE'::character varying])::text[]))
