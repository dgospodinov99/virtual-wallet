package com.team01.web.virtualwallet;

import com.team01.web.virtualwallet.models.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;

public class Helpers {

    public static Card createMockCard() {
        var card = new Card();
        card.setId(1);
        card.setCardNumber("1234567890111213");
        card.setActive(true);
        card.setHolder("Mock User");
        card.setCheckNumber("123");
        card.setExpirationDate(LocalDate.now().plusYears(2));
        card.setUser(new User());

        return card;
    }

    public static User createMockUser() {
        var user = new User();
        user.setWallet(createMockWallet());
        user.setId(1);
        user.setUsername("MockUsername");
        user.setEmail("MockEmail@email.com");
        user.setPassword("MockPassword12#");
        user.setPhoneNumber("0123456789");
        user.setPhotoName(user.getUsername() + ".jpg");
        user.setBlocked(false);
        user.setActive(true);
        var cardList = new HashSet<Card>();
        cardList.add(createMockCard());
        user.setCards(cardList);
        var roleList = new HashSet<Role>();
        roleList.add(createMockRole());
        user.setRoles(roleList);

        return user;
    }

    public static Wallet createMockWallet() {
        var wallet = new Wallet();
        wallet.setId(1);
        wallet.setActive(true);
        wallet.setBalance(100);

        return wallet;
    }

    public static Role createMockRole() {
        var role = new Role();
        role.setId(1);
        role.setName("MockRole");

        return role;
    }

    public static Transaction createMockTransaction() {
        var transaction = new Transaction();
        transaction.setId(1);
        transaction.setAmount(10.0);
        transaction.setSender(createMockWallet());
        var receiver = createMockWallet();
        receiver.setId(2);
        transaction.setReceiver(receiver);
        transaction.setTimestamp(LocalDateTime.now());

        return transaction;
    }

    public static Transfer createMockTransfer() {
        var transfer = new Transfer();
        transfer.setId(1);
        transfer.setTimestamp(LocalDateTime.now());
        transfer.setAmount(100);
        transfer.setWallet(createMockWallet());
        transfer.setCard(createMockCard());

        return transfer;
    }

    public static User createMockAdmin() {
        var user = createMockUser();
        var role = createMockRole();
        role.setName("Administrator");
        var roleList = new HashSet<Role>();
        roleList.add(role);
        user.setRoles(roleList);
        return user;
    }

}
