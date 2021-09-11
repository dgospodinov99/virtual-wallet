insert into `virtual-wallet`.roles (role_id, name)
values  (1, 'User'),
        (2, 'Administrator');

insert into `virtual-wallet`.wallets(wallet_id, balance)
values (1,0),
       (2,0),
       (3,0),
       (4,10000),
       (5,10000);
       
insert into `virtual-wallet`.users (user_id, username, email, phone_number, password, photo_url, wallet_id)
values  (1, 'guygilbert123', 'GuyGilbert@gmail.com', '00000000', 'Parola123@', '',1),
        (2, 'kevinbrown123', 'KevinBrown@gmail.com', '00000001', 'Parola123@', '',2),
        (3, 'roberto123', 'RobertoTamburello@gmail.com', '00000002', 'Parola123@', '',3),
        (4, 'dian.g123', 'DianGospodinov@gmail.com', '00000003', 'Parola123@', '',4),
        (5, 'bojidar.v123', 'BojidarVelichkov@gmail.com', '00000004', 'Parola123@', '',5);

insert into `virtual-wallet`.cards (card_id, card_number, holder, check_number, user_id)
values  (1, '1111111111111111', 'Guy Gilbert', '000', 1),
        (2, '1111111111111112', 'Kevin Brown', '000', 2),
        (3, '1111111111111113', 'Roberto Taamburello', '000', 3),
        (4, '1111111111111114', 'Dian Gospodinov', '000', 4),
        (5, '1111111111111115', 'Bojidar Velichkov', '000', 5);


insert into `virtual-wallet`.users_cards(user_id, card_id)
values  (1,1),
        (2,2),
        (3,3),
        (4,4),
        (5,5);

insert into `virtual-wallet`.users_roles(user_id, role_id)
values  (1,1),
        (2,1),
        (3,1),
        (4,2),
        (5,2);
