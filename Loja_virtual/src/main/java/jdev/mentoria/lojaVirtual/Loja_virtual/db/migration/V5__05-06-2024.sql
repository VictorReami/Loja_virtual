alter table Conta_Pagar add constraint pessoa_fk FOREIGN KEY(pessoa_id) REFERENCES pessoa_fisica(id);

alter table Conta_Pagar add constraint pessoa_fornecedor_fk FOREIGN KEY(pessoa_fornecedor_id) REFERENCES pessoa_juridica(id);

alter table nota_fiscal_Compra add constraint pessoa_fk FOREIGN KEY(pessoa_id) REFERENCES pessoa_fisica(id);