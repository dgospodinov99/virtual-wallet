package com.team01.web.virtualwallet.services;

import com.team01.web.virtualwallet.models.Token;
import com.team01.web.virtualwallet.repositories.contracts.TokenRepository;
import com.team01.web.virtualwallet.services.contracts.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TokenServiceImpl implements TokenService {

    private final TokenRepository tokenRepository;

    @Autowired
    public TokenServiceImpl(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    @Override
    public List<Token> getAllActive() {
        return tokenRepository.getAllActive();
    }

    @Override
    public Token getById(int id) {
        return tokenRepository.getById(id);
    }

    @Override
    public List<Token> getUserTokens(int userId) {
        return tokenRepository.getUserTokens(userId);
    }

    @Override
    public Token getByToken(String token) {
        return tokenRepository.getByToken(token);
    }

    @Override
    public void create(Token token) {
        tokenRepository.create(token);
    }

    @Override
    public void update(Token token) {
        tokenRepository.update(token);
    }

    @Override
    public void delete(int id) {
        Token token = getById(id);
        token.setActive(false);
        tokenRepository.update(token);
    }
}
