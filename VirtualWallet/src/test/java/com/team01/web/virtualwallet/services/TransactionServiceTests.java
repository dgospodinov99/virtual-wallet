package com.team01.web.virtualwallet.services;

import com.team01.web.virtualwallet.exceptions.BlockedUserException;
import com.team01.web.virtualwallet.exceptions.EntityNotFoundException;
import com.team01.web.virtualwallet.exceptions.InvalidTransferException;
import com.team01.web.virtualwallet.exceptions.UnauthorizedOperationException;
import com.team01.web.virtualwallet.models.dto.FilterTransactionByAdminParams;
import com.team01.web.virtualwallet.models.dto.FilterTransactionsByUserParams;
import com.team01.web.virtualwallet.repositories.contracts.TransactionRepository;
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
public class TransactionServiceTests {

    @Mock
    private TransactionRepository mockRepository;

    @InjectMocks
    private TransactionServiceImpl mockService;

    @Mock
    private WalletServiceImpl walletService;

    @Test
    public void getAll_Should_Call_Repository() {
        mockService.getAll();

        Mockito.verify(mockRepository, Mockito.times(1)).getAll();
    }

    @Test
    public void getById_Should_Return_Transaction_If_Match_Exists() {
        //Arrange
        Mockito.when(mockRepository.getById(1))
                .thenReturn(createMockTransaction());
        //Act
        var result = mockService.getById(1);
        //Assert
        Assertions.assertEquals(1, result.getId());
        Assertions.assertEquals(10, result.getAmount());
        Assertions.assertEquals(1, result.getSender().getId());
        Assertions.assertEquals(2, result.getReceiver().getId());
        Assertions.assertEquals(LocalDateTime.now().getYear(), result.getTimestamp().getYear());
        Assertions.assertEquals(LocalDateTime.now().getMonth(), result.getTimestamp().getMonth());
        Assertions.assertEquals(LocalDateTime.now().getDayOfMonth(), result.getTimestamp().getDayOfMonth());
    }

    @Test
    public void getById_Should_Throw_When_No_Match() {
        //Arrange
        Mockito.when(mockRepository.getById(Mockito.anyInt()))
                .thenThrow(EntityNotFoundException.class);
        //Act, Assert
        Assertions.assertThrows(EntityNotFoundException.class,
                () -> mockService.getById(Mockito.anyInt()));
    }

    @Test
    public void getUserTransactions_Should_Call_Repository() {
        //Arrange
        var user = createMockUser();
        //Act
        mockService.getUserTransactions(user, user);
        //Assert
        Mockito.verify(mockRepository, Mockito.times(1))
                .getAllWalletTransactions(user.getWallet());
    }

    @Test
    public void getUserLatestTransactions_Should_Call_Repository() {
        //Arrange
        var user = createMockUser();
        //Act
        mockService.getUserLatestTransactions(user);
        //Assert
        Mockito.verify(mockRepository, Mockito.times(1))
                .getWalletLatestTransactions(user.getWallet());
    }

    @Test
    public void adminFilterTransactions_Should_Call_Repository() {
        //Arrange
        var filterParams = Mockito.any(FilterTransactionByAdminParams.class);
        //Act
        mockService.adminFilterTransactions(filterParams);
        //Assert
        Mockito.verify(mockRepository, Mockito.times(1))
                .adminFilterTransactions(filterParams);
    }

    @Test
    public void userFilterTransactions_Should_Call_Repository() {
        //Arrange
        var filterParams = Mockito.any(FilterTransactionsByUserParams.class);
        //Act
        mockService.userFilterTransactions(filterParams);
        //Assert
        Mockito.verify(mockRepository, Mockito.times(1))
                .userFilterTransactions(filterParams);
    }

    @Test
    public void create_Should_Create_When_Valid_Parameters() {
        //Arrange
        var user = createMockUser();
        var transaction = createMockTransaction();
        //Act
        mockService.create(transaction, user);
        //Assert
        Mockito.verify(mockRepository, Mockito.times(1))
                .create(transaction);
    }

    @Test
    public void create_Should_Throw_When_UserId_InValid() {
        //Arrange
        var user = createMockUser();
        //Act
        user.getWallet().setId(4);
        //Assert
        Assertions.assertThrows(UnauthorizedOperationException.class,
                () -> mockService.create(createMockTransaction(), user));
    }

    @Test
    public void create_Should_Throw_When_Not_Enough_Balance() {
        //Arrange
        var transaction = createMockTransaction();
        //Act
        transaction.getSender().setBalance(0);
        //Assert
        Assertions.assertThrows(InvalidTransferException.class,
                () -> mockService.create(transaction, createMockUser()));
    }

    @Test
    public void create_Should_Throw_When_User_isBlocked(){
        //Arrange
        var user = createMockUser();
        //Act
        user.setBlocked(true);
        //Assert
        Assertions.assertThrows(BlockedUserException.class,
                () -> mockService.create(createMockTransaction(), user));
    }
}
