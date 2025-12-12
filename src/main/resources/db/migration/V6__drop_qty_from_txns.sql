alter table transactions
    drop column quantity;

alter table donations
    add column quantity integer default 0 not null;