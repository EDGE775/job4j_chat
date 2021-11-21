CREATE TABLE roles
(
    id   serial primary key not null,
    role varchar(50)        not null unique
);

create table persons
(
    id       serial primary key not null,
    name     varchar(2000)      not null,
    email    varchar(2000)      not null,
    password varchar(2000)      not null,
    role_id  int                not null,
    FOREIGN KEY (role_id) REFERENCES roles (id)
);

create table rooms
(
    id         serial primary key              not null,
    name       varchar(2000) default 'unknown' not null,
    creator_id int                             not null,
    FOREIGN KEY (creator_id) REFERENCES persons (id)
);

create table messages
(
    id        serial primary key      not null,
    text      text      default ''    not null,
    created   timestamp default now() not null,
    author_id int                     not null,
    room_id   int                     not null,
    FOREIGN KEY (author_id) REFERENCES persons (id),
    FOREIGN KEY (room_id) REFERENCES rooms (id)
);

create table rooms_persons
(
    id        serial primary key not null,
    room_id   int                not null,
    person_id int                not null,
    FOREIGN KEY (room_id) REFERENCES rooms (id),
    FOREIGN KEY (person_id) REFERENCES persons (id)
);

insert into roles (role)
values ('ROLE_USER');
insert into roles (role)
values ('ROLE_ADMIN');
