create table taxi_shipping
(
    id           uuid                        not null,
    destination  jsonb                       not null,
    id_driver    serial,
    id_passenger serial                      not null,
    created_at   timestamp without time zone not null,
    constraint pk_taxi_shipping primary key (id),
    foreign key (id_driver) references user_app (id),
    foreign key (id_passenger) references user_app (id)
)