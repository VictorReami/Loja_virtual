SELECT CONSTRAINT_NAME
FROM information_schema.constraint_column_usage
WHERE table_name = 'usuarios_acesso'
AND column_name = 'acesso_id'
AND constraint_name != 'unique_acesso_user';


alter table usuarios_acesso drop CONSTRAINT "uk_8bak9jswon2id2jbunuqlfl9e";