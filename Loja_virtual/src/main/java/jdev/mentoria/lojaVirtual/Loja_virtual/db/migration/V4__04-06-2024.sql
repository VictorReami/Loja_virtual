CREATE TABLE tabela_acesso_end_potin(
  nome_end_point character varying,
  qtd_acesso_end_point integer);


INSERT INTO tabela_acesso_end_potin(
            nome_end_point, qtd_acesso_end_point)
    VALUES ('END-POINT-NOME-PESSOA-FISICA', 0);


alter table   tabela_acesso_end_potin add constraint nome_end_point_unique UNIQUE (nome_end_point);
