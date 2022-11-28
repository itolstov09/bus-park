
drop table if exists address cascade;
drop table if exists bus cascade;
drop table if exists bus_mechanic cascade;
drop table if exists bus_stop cascade;
drop table if exists employee cascade;
drop table if exists route cascade;
drop table if exists route_bus_stop cascade;


create table address (id  bigserial not null, apartment_number int4 check (apartment_number>=1), street varchar(255) not null, primary key (id));
create table bus (id  bigserial not null, max_passenger int4 not null check (max_passenger>=20 AND max_passenger<=60), model varchar(255) not null, number_plate varchar(255) not null, driver_id int8, route_id int8, primary key (id));
create table bus_mechanic (bus_id int8 not null, mechanic_id int8 not null, primary key (bus_id, mechanic_id));
create table bus_stop (id  bigserial not null, name varchar(255) not null, address_id int8 not null, primary key (id));
create table employee (id  bigserial not null, license_categories varchar(255), license_id varchar(255), last_name varchar(255), middle_name varchar(255), name varchar(255), post varchar(255) not null, salary float8, home_address_id int8 not null, primary key (id));
create table route (id  bigserial not null, route_number int4 not null, primary key (id));
create table route_bus_stop (route_id int8 not null, bus_stop_id int8 not null, primary key (route_id, bus_stop_id));

alter table bus add constraint bus_number_plate_key unique (number_plate);
alter table bus add constraint bus_driver_id_key unique (driver_id);
alter table bus_stop add constraint bus_stop_name_key unique (name);
alter table employee add constraint employee_driver_license_id_key unique (license_id);
alter table route add constraint route_route_number_key unique (route_number);
alter table bus add constraint bus_driver_id_fkey foreign key (driver_id) references employee;
alter table bus add constraint bus_route_id_fkey foreign key (route_id) references route;
alter table bus_mechanic add constraint bus_mechanic_mechanic_id_fkey foreign key (mechanic_id) references employee;
alter table bus_mechanic add constraint bus_mechanic_bus_id_fkey foreign key (bus_id) references bus;
alter table bus_stop add constraint bus_stop_address_id_fkey foreign key (address_id) references address;
alter table employee add constraint employee_home_address_ID_fkey foreign key (home_address_id) references address;
alter table route_bus_stop add constraint route_bus_stop_bus_stop_id_fkey foreign key (bus_stop_id) references bus_stop;
alter table route_bus_stop add constraint route_bus_stop_route_id_fkey foreign key (route_id) references route;
