create table menus
(
    id          bigint not null primary key,
    restaurant_id bigint not null,
    CONSTRAINT FK_RESTAURANT_MENU FOREIGN KEY (restaurant_id) REFERENCES restaurants (id)
);

alter table menus owner to root;

create sequence menu_id_seq;

alter sequence menu_id_seq owner to root;