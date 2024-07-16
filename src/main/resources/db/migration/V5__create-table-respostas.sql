create table respostas (

    id bigint auto_increment,
    mensagem varchar(1000) not null,
    data_criacao datetime not null,
    autor_id bigint,
    topico_id bigint,

    primary key(id),

    foreign key (autor_id) references usuarios(id),
    foreign key (topico_id) references topicos(id) on delete cascade

);