create table temple_requirements
(
    id        bigint generated always as identity (minvalue 33)
        constraint temple_requirements_pk
            primary key,
    item_name text   not null,
    quantity  float4 default 0,
    amount    float4 not null
);

create table temple_bookings
(
    id           bigint generated always as identity (minvalue 25)
        constraint temple_bookings_pk
            primary key,
    item_id      bigint     not null
        constraint temple_bookings_item_fk
            references temple_requirements (id),
    txn_id       bigint     not null
        constraint temple_bookings_txn_fk
            references transactions,
    booking_name text       not null,
    building     varchar(1) not null,
    flat         smallint   not null,
    quantity     float4 default 0,
    amount       float4     not null,
    created_at   timestamp  default now()  not null
);

alter table temple_bookings
    add constraint check_temple_building
        check (temple_bookings.building in ('A', 'B', 'C', 'D', 'E', 'F', 'G'));