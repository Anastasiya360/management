create table management.permissions
(
    id   serial
        constraint permissions_pk
            primary key,
    name varchar not null
);
create unique index permissions_name_ux on management.permissions (name);