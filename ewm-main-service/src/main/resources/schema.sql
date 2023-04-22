drop table if exists compilations;
drop table if exists requests;
drop table if exists compilation_events;
drop table if exists events;
drop table if exists users;
drop table if exists categories;
drop table if exists locations;


create table if not exists users
(
    id    serial primary key,
    email varchar(256) not null unique,
    name  varchar(256) not null

);

create table if not exists compilations
(
    id     serial primary key,
    title  varchar(512) not null,
    pinned boolean      not null
);


create table if not exists categories
(
    id   serial primary key,
    name varchar(512) not null unique
);

create table if not exists locations
(
    id  serial primary key,
    lat float not null unique,
    lon float not null
);

create table if not exists events
(
    id                 serial primary key,
    annotation         varchar(2000)               not null,
    category_id        bigint                      not null,
    created_on         timestamp without time zone not null,
    description        varchar(7000)               not null,
    event_date         timestamp without time zone not null,
    initiator_id       bigint                      not null,
    location_id        bigint                      not null,
    paid               boolean                     not null,
    participant_limit  integer                     not null,
    published_on       timestamp without time zone,
    request_moderation boolean                     not null,
    state              varchar(25)                 not null,
    title              varchar(120)                not null,
    constraint events_categories_id_fk
        foreign key (category_id) references categories,
    constraint events_users_id_fk
        foreign key (initiator_id) references users,
    constraint events_locations_id_fk
        foreign key (location_id) references locations
);

create index if not exists events_event_date_index
    on events (event_date);

create table if not exists requests
(
    id           serial primary key,
    event_id     bigint                      not null,
    requester_id bigint                      not null,
    status       varchar(25)                 not null,
    created      timestamp without time zone not null,
    constraint participation_users_id_fk
        foreign key (requester_id) references users,
    constraint participation_events_events_id_fk
        foreign key (event_id) references events
);

create table if not exists compilation_events
(
    compilation_id bigint not null,
    event_id       bigint not null,
    constraint compilation_events_pk
        primary key (compilation_id, event_id),
    constraint compilation_events_compilations_id_fk
        foreign key (compilation_id) references users,
    constraint compilation_events_events_id_fk
        foreign key (event_id) references events
);
