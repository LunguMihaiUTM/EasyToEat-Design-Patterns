-- Sa se insereze date in tabela bookings
insert into bookings (id, table_id, location_id, booking_status, order_time, no_people, final_price, items, user_id)
values
    (nextval('booking_id_seq'), 1, 1, 'COMPLETED', now(), 2, 35.50,   'Pizza,Meniu Zilei',  1),
    (nextval('booking_id_seq'), 2, 1, 'IN_PROGRESS', now(), 4, 72.00,   'Paste,Burger',  1),
    (nextval('booking_id_seq'), 3, 2, 'CANCELED', now(), 3, 0.00,   'Supa', 1),
    (nextval('booking_id_seq'), 4, 2, 'COMPLETED', now(), 1, 20.00,   'Salata', 1),
    (nextval('booking_id_seq'), 5, 1, 'IN_PROGRESS', now(), 5, 110.00, 'Pizza,Burger,CocaCola', 1);
