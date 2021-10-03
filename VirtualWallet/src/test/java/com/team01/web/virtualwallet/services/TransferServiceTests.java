package com.team01.web.virtualwallet.services;

import com.team01.web.virtualwallet.exceptions.EntityNotFoundException;
import com.team01.web.virtualwallet.repositories.contracts.TransferRepository;
import com.team01.web.virtualwallet.services.contracts.WalletService;
import com.team01.web.virtualwallet.services.services.TransferServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;


import static com.team01.web.virtualwallet.Helpers.*;

@ExtendWith(MockitoExtension.class)
public class TransferServiceTests {

    @Mock
    private TransferRepository mockRepository;

    @InjectMocks
    private TransferServiceImpl mockService;

    @Mock
    private WalletService walletService;

    @Test
    public void getAll_Should_Call_Repository() {
        //Arrange, Act
        mockService.getAll();
        //Assert
        Mockito.verify(mockRepository, Mockito.times(1))
                .getAll();
    }

    @Test
    public void getById_Should_Return_Transfer_When_Match_Found() {
        //Arrange
        Mockito.when(mockRepository.getById(Mockito.anyInt()))
                .thenReturn(createMockTransfer());
        //Act
        var result = mockService.getById(1);
        //Assert
        Assertions.assertEquals(1, result.getId());
        Assertions.assertEquals(100, result.getAmount());
        Assertions.assertEquals(1, result.getWallet().getId());
        Assertions.assertEquals(1, result.getCard().getId());
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
    public void getUserTransfers_Should_Call_Repository() {
        //Arrange
        var user = createMockUser();
        // Act
        mockService.getUserTransfers(user);
        //Assert
        Mockito.verify(mockRepository, Mockito.times(1))
                .getWalletTransfers(user.getWallet());
    }

    @Test
    public void getUserLatestTransfers_Should_Call_Repository() {
        //Arrange
        var user = createMockUser();
        // Act
        mockService.getUserLatestTransfers(user);
        //Assert
        Mockito.verify(mockRepository, Mockito.times(1))
                .getLatestWalletTransfers(user.getWallet());
    }

    @Test
    public void create_Should_Call_Repository() {
        var transfer = createMockTransfer();
        mockService.create(transfer);

        Mockito.verify(mockRepository, Mockito.times(1))
                .create(transfer);
    }
}
