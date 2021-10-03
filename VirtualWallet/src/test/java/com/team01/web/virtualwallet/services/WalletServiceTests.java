package com.team01.web.virtualwallet.services;

import com.team01.web.virtualwallet.exceptions.EntityNotFoundException;
import com.team01.web.virtualwallet.repositories.contracts.WalletRepository;
import com.team01.web.virtualwallet.services.services.WalletServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.team01.web.virtualwallet.Helpers.*;

@ExtendWith(MockitoExtension.class)
public class WalletServiceTests {

    @Mock
    private WalletRepository mockRepository;

    @InjectMocks
    private WalletServiceImpl mockService;

    @Test
    public void getAll_Should_Call_Repository() {
        //Arrange, Act
        mockService.getAll();
        //Assert
        Mockito.verify(mockRepository, Mockito.times(1))
                .getAll();
    }

    @Test
    public void getById_Should_Return_Wallet_When_Match_Exists() {
        //Arrange
        int id = Mockito.anyInt();
        Mockito.when(mockRepository.getById(id))
                .thenReturn(createMockWallet());
        //Act
        var result = mockService.getById(id);
        //Assert
        Assertions.assertEquals(1, result.getId());
        Assertions.assertEquals(true, result.getActive());
        Assertions.assertEquals(100, result.getBalance());
    }

    @Test
    public void getById_Should_Throw_When_No_Match_Found() {
        //Arrange
        int id = Mockito.anyInt();
        Mockito.when(mockRepository.getById(id))
                .thenThrow(EntityNotFoundException.class);
        //Act, Assert
        Assertions.assertThrows(EntityNotFoundException.class,
                () -> mockService.getById(id));
    }

    @Test
    public void create_Should_Call_Repository() {
        //Arrange
        var wallet = createMockWallet();
        // Act
        mockService.create(wallet);
        //Assert
        Mockito.verify(mockRepository, Mockito.times(1))
                .create(wallet);
    }

    @Test
    public void update_Should_Call_Repository() {
        //Arrange
        var wallet = createMockWallet();
        // Act
        mockService.update(wallet);
        //Assert
        Mockito.verify(mockRepository, Mockito.times(1))
                .update(wallet);
    }

    @Test
    public void deposit_Should_Deposit_Amount() {
        //Arrange
        double amount = 100;
        var wallet = createMockWallet();
        double result = wallet.getBalance() + amount;
        // Act
        mockService.deposit(wallet, amount);
        //Assert
        Assertions.assertEquals(result, wallet.getBalance());
    }

    @Test
    public void deposit_Should_Update_Wallet() {
        //Arrange
        var wallet = createMockWallet();
        //Act
        mockService.deposit(wallet, Mockito.anyDouble());
        //Assert
        Mockito.verify(mockRepository, Mockito.times(1))
                .update(wallet);
    }

    @Test
    public void withdraw_Should_Withdraw_Amount() {
        //Arrange
        double amount = 10;
        var wallet = createMockWallet();
        double result = wallet.getBalance() - amount;
        //Act
        mockService.withdraw(wallet, amount);
        //Assert
        Assertions.assertEquals(result, wallet.getBalance());
    }

    @Test
    public void withdraw_Should_Update_Wallet() {
        //Arrange
        double amount = Mockito.anyDouble();
        var wallet = createMockWallet();
        // Act
        mockService.withdraw(wallet, amount);
        //Assert
        Mockito.verify(mockRepository, Mockito.times(1))
                .update(wallet);
    }

    @Test
    public void delete_Should_Make_Wallet_InActive() {
        //Arrange
        var wallet = createMockWallet();
        // Act
        mockService.delete(wallet);
        //Assert
        Assertions.assertFalse(wallet.getActive());
    }
}
