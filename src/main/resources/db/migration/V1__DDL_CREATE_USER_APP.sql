create table user_app
(
    id        serial not null,
    name      varchar(255) not null,
    last_name varchar(255) not null,
    email     varchar(255) not null unique,
    password  varchar(255) not null,
    type      varchar(255) not null,
    constraint pk_user primary key(id)
)