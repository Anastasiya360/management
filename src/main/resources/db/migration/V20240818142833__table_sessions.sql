create table management.sessions
(
    id            serial
        constraint sessions_pk
            primary key,
    user_id       integer not null,
    access_token  varchar not null,
    refresh_token varchar not null,
    date_create    date not null
);
