CREATE TABLE usuarios(

    id bigint not null auto_increment,
    nome varchar(100) not null,
    email varchar(255) not null,
    senha varchar(255) not null,
    ativo tinyint default 1,

    primary key(id)

);