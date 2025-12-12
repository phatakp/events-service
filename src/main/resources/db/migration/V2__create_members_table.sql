create table committee_members
(
    id          bigint generated always as identity
        constraint committee_members_pk
            primary key,
    committee   varchar(20)         not null,
    user_id     varchar(64)         not null,
    is_active   bool        default false not null,

    constraint committee_members_pk2
        unique (committee, user_id)
);

alter table committee_members
    add constraint committee_members_users_slug_fk
        foreign key (user_id) references users(clerk_id);

alter table committee_members
    add constraint check_member_committee
        check (committee_members.committee in ('cultural', 'temple'));