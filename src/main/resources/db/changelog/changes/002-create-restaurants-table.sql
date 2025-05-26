create table restaurants
(
    id              bigint       not null primary key,
    restaurant_name varchar(255) not null,
    logo            varchar(255),
    category_id     bigint,
    CONSTRAINT FK_CATEGORY_RESTAURANT FOREIGN KEY (category_id) REFERENCES categories (id)
);

alter table restaurants owner to root;

create sequence restaurant_id_seq;

alter sequence restaurant_id_seq owner to root;