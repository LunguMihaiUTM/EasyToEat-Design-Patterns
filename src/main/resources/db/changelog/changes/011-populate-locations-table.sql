INSERT INTO locations (id, address, restaurant_id)
VALUES
    -- Andys
    (nextval('location_id_seq'), 'Str. Unirii 12', 1),
    (nextval('location_id_seq'), 'Str. Libertatii 5', 1),

    -- Corso
    (nextval('location_id_seq'), 'Bd. Victoriei 45', 2),
    (nextval('location_id_seq'), 'Str. Mihai Bravu 14', 2),

    -- La Placinte
    (nextval('location_id_seq'), 'Str. Stefan cel Mare 34', 3),
    (nextval('location_id_seq'), 'Str. Florilor 18', 3),

    -- Foisor
    (nextval('location_id_seq'), 'Str. Eminescu 28', 4),
    (nextval('location_id_seq'), 'Bd. Independentei 90', 4),

    -- Torro Burgers
    (nextval('location_id_seq'), 'Str. Republicii 100', 5),
    (nextval('location_id_seq'), 'Str. Libertatii 76', 5),

    -- Pegas Burger
    (nextval('location_id_seq'), 'Bd. Independentei 75', 6),
    (nextval('location_id_seq'), 'Str. Primaverii 44', 6),

    -- Esushi
    (nextval('location_id_seq'), 'Str. Libertatii 22', 7),
    (nextval('location_id_seq'), 'Bd. Revolutiei 30', 7),

    -- Vasabi
    (nextval('location_id_seq'), 'Bd. Mihai Viteazul 67', 8),
    (nextval('location_id_seq'), 'Str. Aviatorilor 101', 8),

    -- Starkebab
    (nextval('location_id_seq'), 'Str. Grivitei 19', 9),
    (nextval('location_id_seq'), 'Str. Dorobantilor 48', 9),

    -- Twister
    (nextval('location_id_seq'), 'Bd. Dacia 88', 10),
    (nextval('location_id_seq'), 'Str. Kogalniceanu 55', 10);
