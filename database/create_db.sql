create table roles
(
    role_id int auto_increment
        primary key,
    name    varchar(35) not null,
    constraint roles_name_uindex
        unique (name)
);

create table wallets
(
    wallet_id int auto_increment
        primary key,
    balance   double     default 0 null,
    is_active tinyint(1) default 1 null
);

create table transfers
(
    transfer_id int auto_increment
        primary key,
    sender_id   int      not null,
    receiver_id int      not null,
    amount      double   not null,
    timestamp   datetime not null,
    constraint transfers_receiver_wallet_id_fk
        foreign key (receiver_id) references wallets (wallet_id),
    constraint transfers_sender_wallet_id_fk
        foreign key (sender_id) references wallets (wallet_id)
);

create table users
(
    user_id      int auto_increment
        primary key,
    is_active    tinyint(1) default 1 null,
    email        varchar(100)         not null,
    phone_number varchar(10)          not null,
    password     varchar(100)         not null,
    photo_url    text                 null,
    wallet_id    int                  not null,
    blocked      tinyint(1) default 0 not null,
    username     varchar(20)          not null,
    constraint users_email_uindex
        unique (email),
    constraint users_phone_number_uindex
        unique (phone_number),
    constraint users_username_uindex
        unique (username),
    constraint users_wallets_wallet_id_fk
        foreign key (wallet_id) references wallets (wallet_id)
);

create table cards
(
    card_id      int auto_increment
        primary key,
    is_active    tinyint(1) default 1 null,
    card_number  varchar(16)          not null,
    holder       varchar(30)          not null,
    check_number varchar(3)           not null,
    user_id      int                  null,
    constraint cards_card_number_uindex
        unique (card_number),
    constraint cards_users_user_id_fk
        foreign key (user_id) references users (user_id)
);

create table users_cards
(
    user_id int not null,
    card_id int not null,
    primary key (user_id, card_id),
    constraint users_cards_cards_card_id_fk
        foreign key (card_id) references cards (card_id),
    constraint users_cards_users_user_id_fk
        foreign key (user_id) references users (user_id)
);

create table users_roles
(
    user_id int not null,
    role_id int not null,
    primary key (user_id, role_id),
    constraint users_roles_roles_role_id_fk
        foreign key (role_id) references roles (role_id),
    constraint users_roles_users_user_id_fk
        foreign key (user_id) references users (user_id)
);

