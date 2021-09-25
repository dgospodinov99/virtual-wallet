package com.team01.web.virtualwallet.services;

import com.team01.web.virtualwallet.exceptions.EntityNotFoundException;
import com.team01.web.virtualwallet.exceptions.UnauthorizedOperationException;
import com.team01.web.virtualwallet.models.Transaction;
import com.team01.web.virtualwallet.models.dto.FilterUserParams;
import com.team01.web.virtualwallet.repositories.contracts.TransactionRepository;
import com.team01.web.virtualwallet.repositories.contracts.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static com.team01.web.virtualwallet.Helpers.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTests {

    @Mock
    private UserRepository mockRepository;

    @InjectMocks
    private UserServiceImpl mockService;

    @Test
    public void getAll_Should_Call_Repository() {
        //Arrange, Act
        mockService.getAll();
        //Assert
        Mockito.verify(mockRepository, Mockito.times(1))
                .getAll();
    }

    @Test
    public void getById_Should_Return_User_When_Match_Exists() {
        //Arrange
        int id = Mockito.anyInt();
        Mockito.when(mockRepository.getById(id))
                .thenReturn(createMockUser());
        //Act
        var result = mockService.getById(id);
        //Assert
        Assertions.assertEquals(1, result.getId());
        Assertions.assertEquals("MockUsername", result.getUsername());
        Assertions.assertEquals("MockEmail@email.com", result.getEmail());
        Assertions.assertEquals("MockPassword12#", result.getPassword());
        Assertions.assertEquals("0123456789", result.getPhoneNumber());
        Assertions.assertEquals("MockUsername.jpg", result.getPhotoURL());
        Assertions.assertTrue(result.getActive());
        Assertions.assertEquals(1, result.getWallet().getId());
        Assertions.assertEquals(1, result.getCards().size());
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
    public void getByUsername_Should_Return_User_When_Match_Exists() {
        //Arrange
        String username = Mockito.anyString();
        Mockito.when(mockRepository.getByUsername(username))
                .thenReturn(createMockUser());
        //Act
        var result = mockService.getByUsername(username);
        //Assert
        Assertions.assertEquals(1, result.getId());
        Assertions.assertEquals("MockUsername", result.getUsername());
        Assertions.assertEquals("MockEmail@email.com", result.getEmail());
        Assertions.assertEquals("MockPassword12#", result.getPassword());
        Assertions.assertEquals("0123456789", result.getPhoneNumber());
        Assertions.assertEquals("MockUsername.jpg", result.getPhotoURL());
        Assertions.assertTrue(result.getActive());
        Assertions.assertEquals(1, result.getWallet().getId());
        Assertions.assertEquals(1, result.getCards().size());
    }

    @Test
    public void getByUsername_Should_Throw_When_No_Match_Found() {
        //Arrange
        String username = Mockito.anyString();
        Mockito.when(mockRepository.getByUsername(username))
                .thenThrow(EntityNotFoundException.class);
        //Act, Assert
        Assertions.assertThrows(EntityNotFoundException.class,
                () -> mockService.getByUsername(username));
    }

    @Test
    public void getByEmail_Should_Return_User_When_Match_Exists() {
        //Arrange
        String email = Mockito.anyString();
        Mockito.when(mockRepository.getByEmail(email))
                .thenReturn(createMockUser());
        //Act
        var result = mockService.getByEmail(email);
        //Assert
        Assertions.assertEquals(1, result.getId());
        Assertions.assertEquals("MockUsername", result.getUsername());
        Assertions.assertEquals("MockEmail@email.com", result.getEmail());
        Assertions.assertEquals("MockPassword12#", result.getPassword());
        Assertions.assertEquals("0123456789", result.getPhoneNumber());
        Assertions.assertEquals("MockUsername.jpg", result.getPhotoURL());
        Assertions.assertTrue(result.getActive());
        Assertions.assertEquals(1, result.getWallet().getId());
        Assertions.assertEquals(1, result.getCards().size());
    }

    @Test
    public void getByEmail_Should_Throw_When_No_Match_Found() {
        //Arrange
        String email = Mockito.anyString();
        Mockito.when(mockRepository.getByEmail(email))
                .thenThrow(EntityNotFoundException.class);
        //Act, Assert
        Assertions.assertThrows(EntityNotFoundException.class,
                () -> mockService.getByEmail(email));
    }

    @Test
    public void getByWallet_Should_Return_User_When_Match_Exists() {
        //Arrange
        var wallet = createMockWallet();
        Mockito.when(mockRepository.getByWallet(wallet.getId()))
                .thenReturn(createMockUser());
        //Act
        var result = mockService.getByWallet(wallet);
        //Assert
        Assertions.assertEquals(1, result.getId());
        Assertions.assertEquals("MockUsername", result.getUsername());
        Assertions.assertEquals("MockEmail@email.com", result.getEmail());
        Assertions.assertEquals("MockPassword12#", result.getPassword());
        Assertions.assertEquals("0123456789", result.getPhoneNumber());
        Assertions.assertEquals("MockUsername.jpg", result.getPhotoURL());
        Assertions.assertTrue(result.getActive());
        Assertions.assertEquals(1, result.getWallet().getId());
        Assertions.assertEquals(1, result.getCards().size());
    }

    @Test
    public void getByWallet_Should_Throw_When_No_Match_Found() {
        //Arrange
        var wallet = createMockWallet();
        Mockito.when(mockRepository.getByWallet(wallet.getId()))
                .thenThrow(EntityNotFoundException.class);
        //Act, Assert
        Assertions.assertThrows(EntityNotFoundException.class,
                () -> mockService.getByWallet(wallet));
    }

    @Test
    public void blockUser_Should_Update_User() {
        //Arrange
        var userToBlock = createMockUser();
        Mockito.when(mockService.getByUsername(userToBlock.getUsername()))
                .thenReturn(userToBlock);
        //Act
        mockService.blockUser(userToBlock.getUsername(), createMockAdmin());
        //Assert
        Mockito.verify(mockRepository, Mockito.times(1))
                .update(userToBlock);
        Assertions.assertTrue(userToBlock.isBlocked());
    }
    @Test
    public void blockUser_Should_Throw_When_Executor_Not_Admin() {
        //Arrange
        var userToBlock = createMockUser();
        Mockito.when(mockService.getByUsername(userToBlock.getUsername()))
                .thenReturn(userToBlock);
        //Act, Assert
        Assertions.assertThrows(UnauthorizedOperationException.class,
                () -> mockService.blockUser(userToBlock.getUsername(), userToBlock));
    }

    @Test
    public void blockUser_Should_Throw_When_User_Doesnt_Exist() {
        //Arrange
        Mockito.when(mockService.getByUsername(Mockito.anyString()))
                .thenThrow(EntityNotFoundException.class);
        //Act, Assert
        Assertions.assertThrows(EntityNotFoundException.class,
                () -> mockService.blockUser(Mockito.anyString(), createMockAdmin()));
    }

    @Test
    public void unblockUser_Should_Update_User() {
        //Arrange
        var userToUnblock = createMockUser();
        Mockito.when(mockService.getByUsername(userToUnblock.getUsername()))
                .thenReturn(userToUnblock);
        //Act
        mockService.unblockUser(userToUnblock.getUsername(), createMockAdmin());
        //Assert
        Mockito.verify(mockRepository, Mockito.times(1))
                .update(userToUnblock);
        Assertions.assertFalse(userToUnblock.isBlocked());
    }
    @Test
    public void unblockUser_Should_Throw_When_Executor_Not_Admin() {
        //Arrange
        var userToUnblock = createMockUser();
        Mockito.when(mockService.getByUsername(userToUnblock.getUsername()))
                .thenReturn(userToUnblock);
        //Act, Assert
        Assertions.assertThrows(UnauthorizedOperationException.class,
                () -> mockService.unblockUser(userToUnblock.getUsername(), userToUnblock));
    }

    @Test
    public void unblockUser_Should_Throw_When_User_Doesnt_Exist() {
        //Arrange
        Mockito.when(mockService.getByUsername(Mockito.anyString()))
                .thenThrow(EntityNotFoundException.class);
        //Act, Assert
        Assertions.assertThrows(EntityNotFoundException.class,
                () -> mockService.unblockUser(Mockito.anyString(), createMockAdmin()));
    }

    @Test
    public void filterUsers_Should_Call_Repository() {
        //Arrange
        var params = Mockito.any(FilterUserParams.class);
        //Act
        mockService.filterUsers(params);
        //Assert
        Mockito.verify(mockRepository, Mockito.times(1))
                .filterUsers(params);
    }

}
