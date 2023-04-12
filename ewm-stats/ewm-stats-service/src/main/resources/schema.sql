create table if not exists stats
(
    id        serial primary key,
    app       varchar(255)                not null,
    uri       varchar(512)                not null,
    ip        varchar(255)                not null,
    timestamp timestamp without time zone not null
);

create index if not exists stats_timestamp_index
    on stats (timestamp);