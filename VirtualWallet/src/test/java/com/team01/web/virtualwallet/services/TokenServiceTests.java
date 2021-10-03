package com.team01.web.virtualwallet.services;


import com.team01.web.virtualwallet.exceptions.EntityNotFoundException;
import com.team01.web.virtualwallet.models.Token;
import com.team01.web.virtualwallet.repositories.contracts.TokenRepository;
import com.team01.web.virtualwallet.services.services.TokenServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.team01.web.virtualwallet.Helpers.*;

@ExtendWith(MockitoExtension.class)
public class TokenServiceTests {

    @Mock
    private TokenRepository mockRepository;

    @InjectMocks
    private TokenServiceImpl mockService;

    @Test
    public void getAll_Should_Call_Repository() {
        //Arrange, Act
        mockService.getAll();
        // Assert
        Mockito.verify(mockRepository, Mockito.times(1))
                .getAll();
    }

    @Test
    public void getById_Should_Return_Card_When_Match_Exists() {
        //Arrange
        Mockito.when(mockRepository.getById(1))
                .thenReturn(createMockToken());
        //Act
        Token result = mockService.getById(1);
        //Assert
        Assertions.assertEquals(1, result.getId());
    }

    @Test
    public void getById_Should_Throw_When_Match_Doesnt_Exist() {
        //Arrange
        Mockito.when(mockRepository.getById(1))
                .thenThrow(EntityNotFoundException.class);
        //Act, Assert
        Assertions.assertThrows(EntityNotFoundException.class,
                () -> mockService.getById(1));
    }

    @Test
    public void getAllActive_Should_Call_Repository() {
        //Arrange, Act
        mockService.getAllActive();
        // Assert
        Mockito.verify(mockRepository, Mockito.times(1))
                .getAllActive();
    }

    @Test
    public void getUserTokens_Should_Call_Repository() {
        //Arrange, Act
        mockService.getUserTokens(createMockUser().getId());
        // Assert
        Mockito.verify(mockRepository, Mockito.times(1))
                .getUserTokens(createMockUser().getId());
    }

    @Test
    public void getByToken_Should_Call_Repository() {
        var token = createMockToken();
        //Arrange, Act
        mockService.getByToken(token.getToken());
        // Assert
        Mockito.verify(mockRepository, Mockito.times(1))
                .getByToken(token.getToken());
    }

    @Test
    public void create_Should_Call_Repository() {
        //Arrange
        var token = createMockToken();

        //Act
        mockService.create(token);
        //Assert
        Mockito.verify(mockRepository, Mockito.times(1))
                .create(Mockito.any(Token.class));
    }

    @Test
    public void update_Should_Call_Repository() {
        //Arrange
        var token = createMockToken();

        //Act
        mockService.update(token);
        //Assert
        Mockito.verify(mockRepository, Mockito.times(1))
                .update(Mockito.any(Token.class));
    }

    @Test
    public void delete_Should_Set_Token_InActive() {
        //Arrange
        Mockito.when(mockRepository.getById(1))
                .thenReturn(createMockToken());
        //Act
        mockService.delete(1);
        //Assert
        Assertions.assertFalse(mockService.getById(1).isActive());
    }

    @Test
    public void isCorrectToken_Should_Return_True_When_Valid() {
        //Arrange
        var token = createMockToken();
        String tokenName = token.getToken();
        Mockito.when(mockRepository.getByToken(tokenName))
                .thenReturn(token);
        //Act
        mockService.isCorrectToken(token, tokenName);
        //Assert
        Assertions.assertTrue(mockService.isCorrectToken(token,tokenName));
    }

}
