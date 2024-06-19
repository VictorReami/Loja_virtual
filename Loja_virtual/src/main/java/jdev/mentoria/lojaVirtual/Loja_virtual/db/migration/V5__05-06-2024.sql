alter table Conta_Pagar add constraint pessoa_fk FOREIGN KEY(pessoa_id) REFERENCES pessoa_fisica(id);

alter table Conta_Pagar add constraint pessoa_fornecedor_fk FOREIGN KEY(pessoa_fornecedor_id) REFERENCES pessoa_juridica(id);

alter table nota_fiscal_Compra add constraint pessoa_fk FOREIGN KEY(pessoa_id) REFERENCES pessoa_fisica(id);

alter table avaliacao_produto add constraint pessoa_fk FOREIGN KEY(pessoa_id) REFERENCES pessoa_fisica(id);

alter table conta_receber add constraint pessoa_fk FOREIGN KEY(pessoa_id) REFERENCES pessoa_fisica(id);

alter table endereco add constraint pessoa_fk FOREIGN KEY(pessoa_id) REFERENCES pessoa_fisica(id);

alter table usuario add constraint pessoa_fk FOREIGN KEY(pessoa_id) REFERENCES pessoa_fisica(id);

alter table vd_cp_loja_virt add constraint pessoa_fk FOREIGN KEY(pessoa_id) REFERENCES pessoa_fisica(id);