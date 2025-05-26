create table locations
(
    id            bigint       not null primary key,
    address       varchar(255) not null,
    restaurant_id bigint not null,
    CONSTRAINT FK_RESTAURANT_LOCATION FOREIGN KEY (restaurant_id) REFERENCES restaurants (id)
);

alter table locations owner to root;

create sequence location_id_seq;

alter sequence location_id_seq owner to root;