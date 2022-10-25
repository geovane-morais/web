create table CLIENTE (
    ID INTEGER PRIMARY KEY AUTO_INCREMENT=1,
    NOME VARCHAR(100),
    CPF VARCHAR(11)
);

create table PRODUTO (
    ID INTEGER PRIMARY KEY AUTO_INCREMENT=1,
    DESCRICAO VARCHAR(100),
    PRECO_UNITARIO NUMERIC(20,2)
);

create table PEDIDO (
    ID INTEGER PRIMARY KEY AUTO_INCREMENT=1,
    CLIENTE_ID INTEGER REFERENCES CLIENTE(ID),
    DATA_PEDIDO TIMESTAMP,
    STATUS VARCHAR(20),
    TOTAL NUMERIC (20,2)
);

create table ITEM_PEDIDO (
    ID INTEGER PRIMARY KEY AUTO_INCREMENT=1,
    PEDIDO_ID INTEGER REFERENCES PEDIDO(ID),
    PRODUTO_ID INTEGER REFERENCES PRODUTO(ID),
    QUANTIDADE INTEGER
);
