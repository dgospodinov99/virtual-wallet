package com.team01.web.virtualwallet.services.utils;

import com.team01.web.virtualwallet.models.Card;
import com.team01.web.virtualwallet.models.Role;
import com.team01.web.virtualwallet.models.User;
import com.team01.web.virtualwallet.models.Wallet;
import com.team01.web.virtualwallet.models.dto.CreateUserDto;
import com.team01.web.virtualwallet.models.dto.UpdateUserDto;
import com.team01.web.virtualwallet.models.dto.UserDto;
import com.team01.web.virtualwallet.services.contracts.CardService;
import com.team01.web.virtualwallet.services.contracts.RoleService;
import com.team01.web.virtualwallet.services.contracts.UserService;
import com.team01.web.virtualwallet.services.contracts.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class UserModelMapper {

    private final UserService userService;
    private final RoleService roleService;
    private final CardService cardService;
    private final WalletService walletService;

    @Autowired
    public UserModelMapper(UserService userService, RoleService roleService, CardService cardService, WalletService walletService) {
        this.userService = userService;
        this.roleService = roleService;
        this.cardService = cardService;
        this.walletService = walletService;
    }


    public User fromCreateDto(CreateUserDto dto) {
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        user.setPhoneNumber(dto.getPhoneNumber());
        Set<Card> cards = new HashSet<>();

        for (int cardId : dto.getCardsId()) {
            cards.add(cardService.getById(cardId));
        }
        user.setCards(cards);


        Role role = roleService.getByName("User");
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        user.setRoles(roles);


        Wallet wallet = walletService.create(new Wallet());
        user.setWallet(wallet);

        return user;
    }

    public User fromUpdateDto(UpdateUserDto dto, int id) {
        User user = userService.getById(id);
        user.setEmail(dto.getEmail());
        user.setPhotoURL(dto.getPhotoURL());
        user.setPassword(dto.getPassword());
        user.setPhoneNumber(dto.getPhoneNumber());
        Set<Card> cards = new HashSet<>();

        for (int cardId : dto.getCardsId()) {
            cards.add(cardService.getById(cardId));
        }
        user.setCards(cards);

        return user;
    }

    public UserDto toDto(User user) {
        return new UserDto(
                user.getUsername(),
                user.getEmail(),
                user.getPhoneNumber(),
                user.getPassword(),
                user.getPhotoURL(),
                user.getWallet().getBalance(),
                user.getCards().stream()
                        .map(Card::getId)
                        .collect(Collectors.toList()));
    }
}
