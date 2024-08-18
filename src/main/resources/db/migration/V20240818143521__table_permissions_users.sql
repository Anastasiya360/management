create table management.table_permissions_users
(
    user_id        integer not null,
    constraint table_table_permissions_users_user_id_fk
        foreign key (user_id) references management.users (id),
    permissions_id integer not null,
    constraint table_table_permissions_users_permissions_id_fk
        foreign key (permissions_id) references management.permissions (id)
);
create index table_table_permissions_users_user_id_ix on management.table_permissions_users (user_id);
create index table_table_permissions_users_permissions_id_ix on management.table_permissions_users (permissions_id);