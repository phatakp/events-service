create table users
(
    clerk_id        varchar(64)                                   not null
        constraint users_pk
            primary key ,
    email           text                                          not null
        constraint users_pk_3
            unique,
    first_name      text                                          not null,
    last_name       text,
    image_url       text,
    role            varchar(10) default 'USER'::character varying not null,
    building        varchar(1)                                    not null,
    flat            smallint                                      not null,
    created_at      timestamp   default now()                     not null,
    updated_at      timestamp   default now()                     not null
);

alter table users
    add constraint check_users_building
        check (users.building in ('A', 'B', 'C', 'D', 'E', 'F', 'G'));

alter table users
    add constraint check_users_role
        check (users.role in ('USER', 'ADMIN'));