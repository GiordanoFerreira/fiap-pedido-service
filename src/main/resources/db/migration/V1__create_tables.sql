CREATE TABLE pedidos (
    id UUID PRIMARY KEY,
    cliente_id BIGINT NOT NULL,
    numero_cartao VARCHAR(50) NOT NULL,
    status_pedido VARCHAR(50) NOT NULL,
    valor_total DECIMAL(19,2) NOT NULL,
    created_at TIMESTAMP DEFAULT now(),
    updated_at TIMESTAMP DEFAULT now(),
    deleted_tmsp TIMESTAMP
);

CREATE TABLE itens_pedido (
    id UUID PRIMARY KEY,
    sku_produto VARCHAR(255) NOT NULL,
    quantidade INTEGER NOT NULL,
    preco_unitario DECIMAL(19,2),
    pedido_id UUID,
    CONSTRAINT fk_pedido FOREIGN KEY (pedido_id) REFERENCES pedidos(id) ON DELETE CASCADE
);