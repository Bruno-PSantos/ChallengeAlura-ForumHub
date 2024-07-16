alter table topicos
 add column autor_id bigint,
 add column curso_id bigint,
 add foreign key (autor_id) references usuarios(id),
 add foreign key (curso_id) references cursos(id);