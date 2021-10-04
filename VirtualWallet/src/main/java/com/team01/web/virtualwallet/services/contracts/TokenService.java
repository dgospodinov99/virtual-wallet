package com.team01.web.virtualwallet.services.contracts;

import com.team01.web.virtualwallet.models.Token;
import com.team01.web.virtualwallet.models.User;

import java.util.List;

public interface TokenService extends BaseGetService<Token> {

    List<Token> getAllActive();

    List<String> getUserTokens(int userId);

    Token getByToken(String token);

    void create(Token token);

    void update(Token token);

    void delete(int id);

    void validateCorrectToken(Token token, User user);
}
