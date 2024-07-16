create table topicos (

    id bigint auto_increment,
    titulo varchar(255) not null unique,
    mensagem varchar(1000) not null,
    data_criacao datetime not null,
    status varchar(20) not null,

    primary key(id)

);