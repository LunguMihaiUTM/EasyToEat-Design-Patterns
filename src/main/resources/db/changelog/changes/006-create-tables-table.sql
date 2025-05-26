create table tables
(
    id          bigint not null primary key,
    location_id bigint,
    is_free     boolean default true,
    CONSTRAINT FK_LOCATION_TABLE FOREIGN KEY (location_id) REFERENCES locations (id)
);

alter table tables owner to root;

create sequence table_id_seq;

alter sequence table_id_seq owner to root;