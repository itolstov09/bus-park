-- EMPLOYEE
insert into address (street, apartment_number) values ('street', 15); -- id 1
insert into address (street, apartment_number) values ('street', 60); -- id 2


insert into employee (name, last_name, salary, post, home_address_id)  -- id 1
    values ('Dmitry', 'Ivanoff', 12000.0, 'MECHANIC', 1);
insert into employee (name, last_name, salary, post, home_address_id)-- id 2
    values ('Mechanic2', 'Ivanoff', 12000.0, 'MECHANIC', 1);
insert into employee (name, last_name, salary, post, home_address_id)-- id 3
    values ('Mechanic3', 'Ivanoff', 12000.0, 'MECHANIC', 1);

insert into employee (name, last_name, salary, post, home_address_id, license_id, license_categories)-- id 4
    values ('Ivan', 'Ivanoff', 15000.0, 'DRIVER', 1, 'DL1234', 'D');
insert into employee (name, last_name, salary, post, home_address_id, license_id, license_categories)-- id 5
    values ('Egor', 'Ivanoff', 15000.0, 'DRIVER', 1, 'DL9874', 'D');
insert into employee (name, last_name, salary, post, home_address_id, license_id, license_categories)-- id 6
    values ('driver2', 'lastName', 15000.0, 'DRIVER', 2, 'DL6251', 'D');


-- BUS STOP
insert into address (street) values ('BS 1 adr'); -- id 3
insert into address (street) values ('BS 2 adr'); -- id 4
insert into address (street) values ('BS 3 adr'); -- id 5
insert into address (street) values ('BS 4 adr'); -- id 6

insert into bus_stop (name, address_id) values ('Bus stop 1', 3); -- id 1
insert into bus_stop (name, address_id) values ('Bus stop 2', 4); -- id 2
insert into bus_stop (name, address_id) values ('Bus stop 3', 5); -- id 3
insert into bus_stop (name, address_id) values ('Bus stop 4', 6); -- id 4


-- ROUTE
insert into route (route_number) values (1); -- id 1
insert into route_bus_stop (route_id, bus_stop_id) values (1, 1);
insert into route_bus_stop (route_id, bus_stop_id) values (1, 2);

insert into route (route_number) values (51); -- id 2
insert into route_bus_stop (route_id, bus_stop_id) values (2, 3);
insert into route_bus_stop (route_id, bus_stop_id) values (2, 4);


-- BUS
insert into bus (model, number_plate, max_passenger, driver_id) -- id 1
    values ('PAZ', '125 ASD 02', 20, 5);
insert into bus_mechanic (bus_id, mechanic_id) values (1, 1);
insert into bus_mechanic (bus_id, mechanic_id) values (1, 3);

insert into bus (model, number_plate, max_passenger, driver_id) -- id 2
    values ('PAZ', '045 NU 02', 40, 6);
insert into bus_mechanic (bus_id, mechanic_id) values (2, 1);
insert into bus_mechanic (bus_id, mechanic_id) values (2, 2);
