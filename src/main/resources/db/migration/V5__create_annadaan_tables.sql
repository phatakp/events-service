create table annadaan_items
(
    id        bigint generated always as identity
        constraint annadaan_pk
            primary key,
    item_name text   not null
        constraint annadaan_pk_2
            unique,
    price           float4 not null,
    quantity        float4 not null,
    amount          float4 not null
    );

create table annadaan_bookings
(
    id             bigint generated always as identity
        constraint annadaan_booking_pk
            primary key,
    item_id        bigint                  not null
        constraint bookings_annadaan_item_id_fk_1
            references annadaan_items (id),
    txn_id         bigint                    not null
        constraint bookings_txn_id_fk_2
            references transactions (id),
    booking_name   text                    not null,
    building       varchar(1)              not null,
    flat           smallint                not null,
    year           smallint default 2025    not null,
    quantity       float4                  not null,
    amount         float4                  not null,
    is_confirmed   boolean   default false not null,
    created_at     timestamp default now() not null,
    constraint annadaan_bookings_pk_2
        unique (item_id, txn_id)
);

alter table annadaan_bookings
    add constraint check_booking_qty
        check (annadaan_bookings.quantity > 0),
    add constraint check_annadaan_building
        check (annadaan_bookings.building in ('A', 'B', 'C', 'D', 'E', 'F', 'G')),
    add constraint check_bookings_year
        check (annadaan_bookings.year >= 2025);