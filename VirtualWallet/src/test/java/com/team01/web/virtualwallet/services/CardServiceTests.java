package com.team01.web.virtualwallet.services;

import com.team01.web.virtualwallet.exceptions.DuplicateEntityException;
import com.team01.web.virtualwallet.exceptions.EntityNotFoundException;
import com.team01.web.virtualwallet.exceptions.InvalidCardInformation;
import com.team01.web.virtualwallet.exceptions.UnauthorizedOperationException;
import com.team01.web.virtualwallet.models.Card;
import com.team01.web.virtualwallet.repositories.contracts.CardRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static com.team01.web.virtualwallet.Helpers.*;

@ExtendWith(MockitoExtension.class)
public class CardServiceTests {

    @Mock
    private CardRepository mockRepository;

    @InjectMocks
    private CardServiceImpl mockService;

    @Test
    public void getAll_Should_Call_Repository() {
        //Arrange, Act
        mockService.getAll(createMockAdmin());
        // Assert
        Mockito.verify(mockRepository, Mockito.times(1))
                .getAll();
    }

    @Test
    public void getById_Should_Return_Card_When_Match_Exists() {
        //Arrange
        Mockito.when(mockRepository.getById(1))
                .thenReturn(createMockCard());
        //Act
        Card result = mockService.getById(1);
        //Assert
        Assertions.assertEquals(1, result.getId());
        Assertions.assertEquals("1234567890111213", result.getCardNumber());
        Assertions.assertEquals("Mock User", result.getHolder());
        Assertions.assertEquals("123", result.getCheckNumber());
        Assertions.assertEquals(LocalDateTime.now().plusYears(2).getYear(), result.getExpirationDate().getYear());
        Assertions.assertEquals(LocalDateTime.now().getMonth(), result.getExpirationDate().getMonth());
        Assertions.assertEquals(LocalDateTime.now().getDayOfMonth(), result.getExpirationDate().getDayOfMonth());
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
    public void getByCardNumber_Should_Return_Card_When_Match_Exists() {
        //Arrange
        Mockito.when(mockRepository.getByCardNumber("1234567890111213"))
                .thenReturn(createMockCard());
        //Act
        Card result = mockService.getByCardNumber("1234567890111213");
        //Assert
        Assertions.assertEquals(1, result.getId());
        Assertions.assertEquals("1234567890111213", result.getCardNumber());
        Assertions.assertEquals("Mock User", result.getHolder());
        Assertions.assertEquals("123", result.getCheckNumber());
        Assertions.assertEquals(LocalDateTime.now().plusYears(2).getYear(), result.getExpirationDate().getYear());
        Assertions.assertEquals(LocalDateTime.now().getMonth(), result.getExpirationDate().getMonth());
        Assertions.assertEquals(LocalDateTime.now().getDayOfMonth(), result.getExpirationDate().getDayOfMonth());
    }

    @Test
    public void getByCardNumber_Should_Throw_When_Match_Doesnt_Exist() {
        //Arrange
        Mockito.when(mockRepository.getByCardNumber("1234567890111213"))
                .thenThrow(EntityNotFoundException.class);
        //Act, Assert
        Assertions.assertThrows(EntityNotFoundException.class,
                () -> mockService.getByCardNumber("1234567890111213"));
    }

    @Test
    public void getByCardNumber_Should_Throw_If_CardNumber_Is_Invalid() {
        //Arrange, Act, Assert
        Assertions.assertThrows(InvalidCardInformation.class,
                () -> mockService.getByCardNumber("Invalid Number"));
    }

    @Test
    public void create_Should_Call_Repository_When_CardNumber_Unique() {
        //Arrange
        var card = createMockCard();
        Mockito.when(mockRepository.getByCardNumber(card.getCardNumber()))
                .thenThrow(EntityNotFoundException.class);
        //Act
        mockService.create(card);
        //Assert
        Mockito.verify(mockRepository, Mockito.times(1))
                .create(Mockito.any(Card.class));
    }

    @Test
    public void create_Should_Throw_When_Card_Exists() {
        //Arrange, Act, Assert
        var card = createMockCard();
        Assertions.assertThrows(DuplicateEntityException.class,
                () -> mockService.create(card));
    }

    @Test
    public void update_Should_Update_When_Data_is_Valid() {
        //Arrange
        var admin = createMockAdmin();
        var card = createMockCard();
        //Act
        mockService.update(card, admin);
        //Assert
        Mockito.verify(mockRepository, Mockito.times(1))
                .update(Mockito.any(Card.class));
    }

    @Test
    public void update_Should_Throw_When_User_Is_Not_Admin() {
        //Arrange
        var card = createMockCard();
        var user = createMockUser();
        //Act, Assert
        Assertions.assertThrows(UnauthorizedOperationException.class,
                () -> mockService.update(card, user));
    }

    @Test
    public void update_Should_Throw_When_Invalid_CardNumber() {
        //Arrange
        var card = createMockCard();
        card.setCardNumber("Invalid Number");
        //Act, Assert
        Assertions.assertThrows(InvalidCardInformation.class,
                () -> mockService.update(card, createMockAdmin()));
    }

    @Test
    public void delete_Should_Set_Card_InActive_When_User_Is_Admin() {
        //Arrange
        Mockito.when(mockRepository.getById(1))
                .thenReturn(createMockCard());
        //Act
        mockService.delete(1, createMockAdmin());
        //Assert
        Assertions.assertFalse(mockService.getById(1).isActive());
    }

    @Test
    public void delete_Should_Throw_When_User_Is_Not_Admin() {
        //Arrange
        Mockito.when(mockRepository.getById(1))
                .thenReturn(createMockCard());
        // Act, Assert
        Assertions.assertThrows(UnauthorizedOperationException.class,
                () -> mockService.delete(1, createMockUser()));
    }

}
