create table transactions
(
    id           bigint generated always as identity
        constraint transactions_pk
            primary key,
    year         smallint                not null,
    description  text                    ,
    amount       float4                  not null,
    date        date default CURRENT_DATE not null,
    committee    varchar(20)             not null ,

    user_id   varchar(64)            not null
        constraint txns_users_fk_2
            references users (clerk_id),
    txn_type       varchar(20) default 'donation' not null,
    txn_sub_type   varchar(20),
    txn_mode       varchar(20) default 'online' not null,
    quantity       integer default 0 not null,
    created_at     timestamp default now() not null
);

alter table transactions
    add constraint check_committee
        check (transactions.committee in ('cultural', 'temple')),
    add constraint check_txn_sub_type
        check (transactions.txn_sub_type in ('additional','annadaan','itemized',null)),
    add constraint check_txn_type
        check (transactions.txn_type in ('donation', 'expense','transfer_in', 'transfer_out')),
    add constraint check_txn_mode
        check (transactions.txn_mode in ('cash', 'online')),
    add constraint check_year
        check (transactions.year >=2025);