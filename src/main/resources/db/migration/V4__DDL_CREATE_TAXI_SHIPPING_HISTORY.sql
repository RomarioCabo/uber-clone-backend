create table taxi_shipping_history
(
    id               uuid                        not null,
    id_taxi_shipping uuid                        not null,
    status_route     varchar(255)                not null,
    event_date       timestamp without time zone not null,
    constraint pk_taxi_shipping_history primary key (id)
)