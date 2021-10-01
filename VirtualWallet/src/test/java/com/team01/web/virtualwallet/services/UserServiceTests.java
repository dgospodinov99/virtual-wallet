package com.team01.web.virtualwallet.services;

import com.team01.web.virtualwallet.exceptions.DuplicateEntityException;
import com.team01.web.virtualwallet.exceptions.EntityNotFoundException;
import com.team01.web.virtualwallet.exceptions.InvalidPasswordException;
import com.team01.web.virtualwallet.exceptions.UnauthorizedOperationException;
import com.team01.web.virtualwallet.models.dto.FilterUserParams;
import com.team01.web.virtualwallet.repositories.contracts.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.team01.web.virtualwallet.Helpers.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTests {

    @Mock
    private UserRepository mockRepository;

    @Mock
    private WalletServiceImpl mockWalletService;

    @Mock
    private CardServiceImpl mockCardService;

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
        Assertions.assertEquals("MockUsername.jpg", result.getPhotoName());
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
        Assertions.assertEquals("MockUsername.jpg", result.getPhotoName());
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
        Assertions.assertEquals("MockUsername.jpg", result.getPhotoName());
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
        Assertions.assertEquals("MockUsername.jpg", result.getPhotoName());
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
        mockService.blockUserByAdmin(userToBlock.getUsername(), createMockAdmin());
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
                () -> mockService.blockUserByAdmin(userToBlock.getUsername(), userToBlock));
    }

    @Test
    public void blockUser_Should_Throw_When_User_Doesnt_Exist() {
        //Arrange
        Mockito.when(mockService.getByUsername(Mockito.anyString()))
                .thenThrow(EntityNotFoundException.class);
        //Act, Assert
        Assertions.assertThrows(EntityNotFoundException.class,
                () -> mockService.blockUserByAdmin(Mockito.anyString(), createMockAdmin()));
    }

    @Test
    public void unblockUser_Should_Update_User() {
        //Arrange
        var userToUnblock = createMockUser();
        Mockito.when(mockService.getByUsername(userToUnblock.getUsername()))
                .thenReturn(userToUnblock);
        //Act
        mockService.unblockUserByAdmin(userToUnblock.getUsername(), createMockAdmin());
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
                () -> mockService.unblockUserByAdmin(userToUnblock.getUsername(), userToUnblock));
    }

    @Test
    public void unblockUser_Should_Throw_When_User_Doesnt_Exist() {
        //Arrange
        Mockito.when(mockService.getByUsername(Mockito.anyString()))
                .thenThrow(EntityNotFoundException.class);
        //Act, Assert
        Assertions.assertThrows(EntityNotFoundException.class,
                () -> mockService.unblockUserByAdmin(Mockito.anyString(), createMockAdmin()));
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

    @Test
    public void create_Should_Call_Repository_When_Valid_Parameters() {
        //Arrange
        var user = createMockUser();
        Mockito.when(mockRepository.getByEmail(user.getEmail()))
                .thenThrow(EntityNotFoundException.class);
        Mockito.when(mockRepository.getByUsername(user.getUsername()))
                .thenThrow(EntityNotFoundException.class);
        Mockito.when(mockRepository.getByPhoneNumber(user.getPhoneNumber()))
                .thenThrow(EntityNotFoundException.class);
        //Act
        mockService.create(user);
        //Assert
        Mockito.verify(mockRepository, Mockito.times(1))
                .create(user);
    }

    @Test
    public void create_Should_Throw_When_Username_Not_Unique() {
        //Arrange
        var user = createMockUser();
        var user2 = createMockUser();
        user2.setId(2);
        Mockito.when(mockRepository.getByEmail(user.getEmail()))
                .thenThrow(EntityNotFoundException.class);
        Mockito.when(mockRepository.getByUsername(user.getUsername()))
                .thenReturn(user2);
        //Act, Assert
        Assertions.assertThrows(DuplicateEntityException.class,
                () -> mockService.create(user));
    }

    @Test
    public void create_Should_Throw_When_Email_Not_Unique() {
        //Arrange
        var user = createMockUser();
        var user2 = createMockUser();
        user2.setId(2);
        Mockito.when(mockRepository.getByEmail(user.getEmail()))
                .thenReturn(user2);
        //Act, Assert
        Assertions.assertThrows(DuplicateEntityException.class,
                () -> mockService.create(user));
    }

    @Test
    public void create_Should_Throw_When_PhoneNumber_Not_Unique() {
        //Arrange
        var user = createMockUser();
        var user2 = createMockUser();
        user2.setId(2);
        Mockito.when(mockRepository.getByEmail(user.getEmail()))
                .thenThrow(EntityNotFoundException.class);
        Mockito.when(mockRepository.getByUsername(user.getUsername()))
                .thenThrow(EntityNotFoundException.class);
        Mockito.when(mockRepository.getByPhoneNumber(user.getPhoneNumber()))
                .thenReturn(user2);
        //Act, Assert
        Assertions.assertThrows(DuplicateEntityException.class,
                () -> mockService.create(user));
    }

    @Test
    public void create_Should_Throw_When_Password_Too_Short() {
        //Arrange
        var user = createMockUser();
        Mockito.when(mockRepository.getByEmail(user.getEmail()))
                .thenThrow(EntityNotFoundException.class);
        Mockito.when(mockRepository.getByUsername(user.getUsername()))
                .thenThrow(EntityNotFoundException.class);
        Mockito.when(mockRepository.getByPhoneNumber(user.getPhoneNumber()))
                .thenThrow(EntityNotFoundException.class);
        //Act
        user.setPassword("Aa1@");
        Assertions.assertThrows(InvalidPasswordException.class,
                () -> mockService.create(user));
    }

    @Test
    public void create_Should_Throw_When_Password_Doesnt_Contain_Special_Symbol() {
        //Arrange
        var user = createMockUser();
        Mockito.when(mockRepository.getByEmail(user.getEmail()))
                .thenThrow(EntityNotFoundException.class);
        Mockito.when(mockRepository.getByUsername(user.getUsername()))
                .thenThrow(EntityNotFoundException.class);
        Mockito.when(mockRepository.getByPhoneNumber(user.getPhoneNumber()))
                .thenThrow(EntityNotFoundException.class);
        //Act
        user.setPassword("Aaaaaaaaa12");
        //Assert
        Assertions.assertThrows(InvalidPasswordException.class,
                () -> mockService.create(user));
    }

    @Test
    public void create_Should_Throw_When_Password_Doesnt_Contain_Capital_Letter() {
        //Arrange
        var user = createMockUser();
        Mockito.when(mockRepository.getByEmail(user.getEmail()))
                .thenThrow(EntityNotFoundException.class);
        Mockito.when(mockRepository.getByUsername(user.getUsername()))
                .thenThrow(EntityNotFoundException.class);
        Mockito.when(mockRepository.getByPhoneNumber(user.getPhoneNumber()))
                .thenThrow(EntityNotFoundException.class);
        //Act
        user.setPassword("aaaaaaaaa12#");
        //Assert
        Assertions.assertThrows(InvalidPasswordException.class,
                () -> mockService.create(user));
    }

    @Test
    public void create_Should_Throw_When_Password_Doesnt_Contain_Lowercase_Letter() {
        //Arrange
        var user = createMockUser();
        Mockito.when(mockRepository.getByEmail(user.getEmail()))
                .thenThrow(EntityNotFoundException.class);
        Mockito.when(mockRepository.getByUsername(user.getUsername()))
                .thenThrow(EntityNotFoundException.class);
        Mockito.when(mockRepository.getByPhoneNumber(user.getPhoneNumber()))
                .thenThrow(EntityNotFoundException.class);
        //Act
        user.setPassword("AAAAAAAAA12#");
        //Assert
        Assertions.assertThrows(InvalidPasswordException.class,
                () -> mockService.create(user));
    }

    @Test
    public void create_Should_Throw_When_Password_Doesnt_Contain_Digit() {
        //Arrange
        var user = createMockUser();
        Mockito.when(mockRepository.getByEmail(user.getEmail()))
                .thenThrow(EntityNotFoundException.class);
        Mockito.when(mockRepository.getByUsername(user.getUsername()))
                .thenThrow(EntityNotFoundException.class);
        Mockito.when(mockRepository.getByPhoneNumber(user.getPhoneNumber()))
                .thenThrow(EntityNotFoundException.class);
        //Act
        user.setPassword("AAAAAAAAAaa#");
        //Assert
        Assertions.assertThrows(InvalidPasswordException.class,
                () -> mockService.create(user));
    }

    @Test
    public void create_Should_Create_When_Password_Is_Valid() {
        //Arrange
        var user = createMockUser();
        Mockito.when(mockRepository.getByEmail(user.getEmail()))
                .thenThrow(EntityNotFoundException.class);
        Mockito.when(mockRepository.getByUsername(user.getUsername()))
                .thenThrow(EntityNotFoundException.class);
        Mockito.when(mockRepository.getByPhoneNumber(user.getPhoneNumber()))
                .thenThrow(EntityNotFoundException.class);
        //Act
        user.setPassword("AAAAAAAAAaa12#");
        mockService.create(user);
        //Assert
        Mockito.verify(mockRepository, Mockito.times(1))
                .create(user);
    }

    @Test
    public void delete_Should_Call_Repository() {
        //Arrange
        var user = createMockUser();
        Mockito.when(mockRepository.getById(user.getId()))
                .thenReturn(user);
        //Act
        mockService.delete(user.getId());
        //Assert
        Mockito.verify(mockRepository, Mockito.times(1))
                .update(user);
        Assertions.assertFalse(user.getActive());
    }

    @Test
    public void delete_Should_Throw_When_No_Match_Found() {
        //Arrange
        Mockito.when(mockRepository.getById(Mockito.anyInt()))
                .thenThrow(EntityNotFoundException.class);
        //Act, Assert
        Assertions.assertThrows(EntityNotFoundException.class,
                () -> mockService.delete(Mockito.anyInt()));
    }


    @Test
    public void updatePassword_Should_Throw_When_Password_Too_Short() {
        //Arrange
        var user = createMockUser();
        String invalidPassword = "Aa1@";

        //Act, Assert
        Assertions.assertThrows(InvalidPasswordException.class,
                () -> mockService.updatePassword(user, invalidPassword));
    }

    @Test
    public void updatePassword_Should_Throw_When_Password_Doesnt_Contain_Special_Symbol() {
        //Arrange
        var user = createMockUser();
        String invalidPassword = "Aaaaaaaaa12";

        //Act , Assert
        Assertions.assertThrows(InvalidPasswordException.class,
                () -> mockService.updatePassword(user, invalidPassword));
    }

    @Test
    public void updatePassword_Should_Throw_When_Password_Doesnt_Contain_Capital_Letter() {
        //Arrange
        var user = createMockUser();
        String invalidPassword = "aaaaaaaaa12#";

        //Act ,Assert
        Assertions.assertThrows(InvalidPasswordException.class,
                () -> mockService.updatePassword(user,invalidPassword));
    }

    @Test
    public void updatePassword_Should_Throw_When_Password_Doesnt_Contain_Lowercase_Letter() {
        //Arrange
        var user = createMockUser();

        String invalidPassword = "AAAAAAAAA12#";
        //Act, Assert
        Assertions.assertThrows(InvalidPasswordException.class,
                () -> mockService.updatePassword(user, invalidPassword));
    }

    @Test
    public void updatePassword_Should_Throw_When_Password_Doesnt_Contain_Digit() {
        //Arrange
        var user = createMockUser();
        String invalidPassword = "AAAAAAAAAaa#";

        //Act, Assert
        Assertions.assertThrows(InvalidPasswordException.class,
                () -> mockService.updatePassword(user, invalidPassword));
    }

    @Test
    public void updatePassword_Should_Update_When_Password_Is_Valid() {
        //Arrange
        var user = createMockUser();
        String validPassword = "AAAAAAAAAaa12#";

        //Act
        mockService.updatePassword(user, validPassword);

        //Assert
        Mockito.verify(mockRepository, Mockito.times(1))
                .update(user);
    }

}
