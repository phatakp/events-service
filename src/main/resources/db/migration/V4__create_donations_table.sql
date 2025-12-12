create table donations
(
    id           bigint generated always as identity
        constraint donations_pk
            primary key,
    txn_id           bigint                  not null
        constraint donations_txn_id_fk_1
            references transactions(id),
    donor_name  text                        not null,
    building   varchar(1)                   not null,
    flat       smallint                     not null,
    year        smallint                not null

);

alter table donations
    add constraint check_donations_building
        check (donations.building in ('A', 'B', 'C', 'D', 'E', 'F', 'G'));

alter table donations
    add constraint check_donations_year
        check (donations.year >= 2025);