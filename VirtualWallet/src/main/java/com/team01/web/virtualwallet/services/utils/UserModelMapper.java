package com.team01.web.virtualwallet.services.utils;

import com.team01.web.virtualwallet.models.Card;
import com.team01.web.virtualwallet.models.Role;
import com.team01.web.virtualwallet.models.User;
import com.team01.web.virtualwallet.models.dto.UserDto;
import com.team01.web.virtualwallet.services.contracts.CardService;
import com.team01.web.virtualwallet.services.contracts.RoleService;
import com.team01.web.virtualwallet.services.contracts.UserService;
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

    @Autowired
    public UserModelMapper(UserService userService, RoleService roleService, CardService cardService) {
        this.userService = userService;
        this.roleService = roleService;
        this.cardService = cardService;
    }

    public User fromDto(UserDto dto) {
        User user = new User();
        user.setEmail(dto.getEmail());
        user.setUsername(dto.getUsername());
        user.setPassword(dto.getPassword());
        user.setPhotoURL(dto.getPhotoURL());
        user.setPhoneNumber(dto.getPhoneNumber());
        user.setBalance(dto.getBalance());
        Role role = roleService.getByName("User");
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        user.setRoles(roles);
        Set<Card> cards = new HashSet<>();
        for (int cardId : dto.getCardsId()) {
            cards.add(cardService.getById(cardId));
        }
        user.setCards(cards);

        return user;
    }

    public User fromDto(UserDto dto, int id) {
        User user = userService.getById(id);
        user.setBalance(dto.getBalance());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        user.setPhotoURL(dto.getPhotoURL());
        return user;
    }

    public UserDto toDto(User user) {
        return new UserDto(
                user.getUsername(),
                user.getEmail(),
                user.getPhoneNumber(),
                user.getPassword(),
                user.getPhotoURL(),
                user.getBalance(),
                user.getCards().stream()
                        .map(Card::getId)
                        .collect(Collectors.toList()));
    }
}
