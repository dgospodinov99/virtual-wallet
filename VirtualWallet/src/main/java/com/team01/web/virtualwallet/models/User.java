package com.team01.web.virtualwallet.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private int id;

    @Column(name = "username")
    private String username;

    @Email
    @Column(name = "email")
    private String email;

    @Column(name = "phone_number")
    private String phoneNumber;

    @JsonIgnore
    @Column(name = "password")
    private String password;

    @Column(name = "photo_name", insertable = false)
    private String photoName;

    @Column(name = "blocked")
    private boolean blocked;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "wallet_id")
    private Wallet wallet;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "users_cards",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "card_id")
    )
    private Set<Card> cards;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles;

    @Column(name = "is_active")
    private Boolean active;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhotoName() {
        return photoName;
    }

    public void setPhotoName(String photoURL) {
        this.photoName = photoURL;
    }

    public Wallet getWallet() {
        return wallet;
    }

    public void setWallet(Wallet walletId) {
        this.wallet = walletId;
    }

    public Set<Card> getCards() {
        Set<Card> activeCards = new HashSet<>();
        for (Card card : cards) {
            if (card.isActive()) {
                activeCards.add(card);
            }
        }
        return activeCards;
    }

    public void setCards(Set<Card> cards) {
        this.cards = cards;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public boolean isBlocked() {
        return blocked;
    }

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }

    public Boolean getActive() {
        return active;
    }

    public User setActive(Boolean active) {
        this.active = active;
        return this;
    }

    public boolean isAdmin() {
        return getRoles().stream()
                .anyMatch(role -> role.getName().equals("Administrator"));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return this.id == user.id && Objects.equals(this.username, user.username);
    }
}
