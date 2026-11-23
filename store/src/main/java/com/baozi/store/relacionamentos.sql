USE baozi_store;

ALTER TABLE pedidos
ADD CONSTRAINT fk_pedido_cliente
FOREIGN KEY (cliente_id) REFERENCES clientes(id);

ALTER TABLE pedidos
ADD CONSTRAINT fk_pedido_produto
FOREIGN KEY (produto_id) REFERENCES produtos(id);