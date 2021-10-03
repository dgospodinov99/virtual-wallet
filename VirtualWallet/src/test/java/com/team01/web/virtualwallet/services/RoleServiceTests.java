package com.team01.web.virtualwallet.services;

import com.team01.web.virtualwallet.exceptions.EntityNotFoundException;
import com.team01.web.virtualwallet.repositories.contracts.RoleRepository;
import com.team01.web.virtualwallet.services.services.RoleServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.team01.web.virtualwallet.Helpers.*;

@ExtendWith(MockitoExtension.class)
public class RoleServiceTests {

    @Mock
    private RoleRepository mockRepository;

    @InjectMocks
    private RoleServiceImpl mockService;

    @Test
    public void getAll_Should_Call_Repository() {
        //Arrange, Act
        mockService.getAll();
        //Assert
        Mockito.verify(mockRepository, Mockito.times(1))
                .getAll();
    }

    @Test
    public void getById_Should_Return_Role_When_Match_Exists() {
        //Arrange
        Mockito.when(mockRepository.getById(1))
                .thenReturn(createMockRole());
        //Act
        var result = mockService.getById(1);
        //Assert
        Assertions.assertEquals(1, result.getId());
        Assertions.assertEquals("MockRole", result.getName());
    }

    @Test
    public void getById_Should_Throw_When_No_Match() {
        //Arrange
        Mockito.when(mockRepository.getById(1)).thenThrow(EntityNotFoundException.class);
        //Act, Assert
        Assertions.assertThrows(EntityNotFoundException.class,
                () -> mockService.getById(1));
    }

    @Test
    public void getByName_Should_Return_Role_When_Match_Exists() {
        //Arrange
        Mockito.when(mockRepository.getByName(Mockito.anyString()))
                .thenReturn(createMockRole());
        //Act
        var result = mockService.getByName("Role name");
        //Assert
        Assertions.assertEquals(1, result.getId());
        Assertions.assertEquals("MockRole", result.getName());
    }

    @Test
    public void getByName_Should_Throw_When_No_Match() {
        //Arrange
        Mockito.when(mockRepository.getByName(Mockito.anyString()))
                .thenThrow(EntityNotFoundException.class);
        //Act, Assert
        Assertions.assertThrows(EntityNotFoundException.class,
                () -> mockService.getByName("MockRole"));
    }

}
