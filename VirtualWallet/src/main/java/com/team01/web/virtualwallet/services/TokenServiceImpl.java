package com.team01.web.virtualwallet.services;

import com.team01.web.virtualwallet.models.Token;
import com.team01.web.virtualwallet.repositories.contracts.TokenRepository;
import com.team01.web.virtualwallet.services.contracts.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TokenServiceImpl extends BaseGetServiceImpl<Token> implements TokenService {

    private final TokenRepository tokenRepository;

    @Autowired
    public TokenServiceImpl(TokenRepository tokenRepository) {
        super(tokenRepository);
        this.tokenRepository = tokenRepository;
    }

    @Override
    public List<Token> getAllActive() {
        return tokenRepository.getAllActive();
    }

    @Override
    public List<String> getUserTokens(int userId) {
        return tokenRepository.getUserTokens(userId).stream()
                .map(token -> token.getToken())
                .collect(Collectors.toList());
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

    @Override
    public boolean isCorrectToken(Token token, String toCompare) {
        return token.equals(tokenRepository.getByToken(toCompare));
    }


}
