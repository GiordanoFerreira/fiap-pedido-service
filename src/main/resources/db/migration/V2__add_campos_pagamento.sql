ALTER TABLE pedidos
ADD COLUMN codigo_seguranca_cartao VARCHAR(4),
ADD COLUMN nome_titular_cartao VARCHAR(100),
ADD COLUMN data_validade DATE;