package com.team01.web.virtualwallet.repositories.contracts;

import com.team01.web.virtualwallet.models.Token;
import com.team01.web.virtualwallet.models.User;

import java.util.List;

public interface TokenRepository {

    List<Token> getAllActive();

    Token getById(int id);

    List<Token> getUserTokens(int userId);

    Token getByToken(String token);

    void create(Token token);

    void update(Token token);

    void delete(int id);

}
