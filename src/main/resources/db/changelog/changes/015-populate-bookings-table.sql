-- Sa se insereze date in tabela bookings
insert into bookings (id, table_id, location_id, booking_status, order_time, no_people, final_price, name, phone_number, items, mail)
values
    (nextval('booking_id_seq'), 1, 1, 'COMPLETED', now(), 2, 35.50, 'Ion Popescu', '0700000001', 'Pizza,Meniu Zilei', 'ion@example.com'),
    (nextval('booking_id_seq'), 2, 1, 'IN_PROGRESS', now(), 4, 72.00, 'Maria Ionescu', '0700000002', 'Paste,Burger', 'maria@example.com'),
    (nextval('booking_id_seq'), 3, 2, 'CANCELED', now(), 3, 0.00, 'Andrei Georgescu', '0700000003', 'Supa', 'andrei@example.com'),
    (nextval('booking_id_seq'), 4, 2, 'COMPLETED', now(), 1, 20.00, 'Elena Dinu', '0700000004', 'Salata', 'elena@example.com'),
    (nextval('booking_id_seq'), 5, 1, 'IN_PROGRESS', now(), 5, 110.00, 'Cristian Mihai', '0700000005', 'Pizza,Burger,CocaCola', 'cristian@example.com');
