create table bookings
(
    id             bigint not null primary key,
    table_id       bigint,
    location_id    bigint not null,
    booking_status varchar(255),
    order_time     timestamp,
    no_people      int,
    final_price    double precision,
    name           varchar(255),
    phone_number   varchar(255),
    items          varchar(255),
    mail           varchar(255),
    CONSTRAINT FK_TABLES_BOOKING FOREIGN KEY (table_id) REFERENCES tables (id),
    CONSTRAINT FK_LOCATIONS_BOOKING FOREIGN KEY (location_id) REFERENCES locations (id)

);

alter table bookings owner to root;

create sequence booking_id_seq;

alter sequence booking_id_seq owner to root;