package com.mega.city.cab.backend.service;


import com.megacityCab.megaCityCabBackEnd.dto.DriverDto;
import com.megacityCab.megaCityCabBackEnd.entity.Driver;
import com.megacityCab.megaCityCabBackEnd.repo.DriverRepo;
import com.megacityCab.megaCityCabBackEnd.service.impl.DriverServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.security.authentication.BadCredentialsException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class DriverServiceImplTest {

    @Mock
    private DriverRepo driverRepo;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private DriverServiceImpl driverService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveDriver_Success() {
        // Arrange
        DriverDto driverDto = new DriverDto();
        driverDto.setEmail("test@example.com");
        driverDto.setNic("123456789");
        driverDto.setLicenseNumber("LIC123");
        driverDto.setContactNumber("1234567890");

        Driver driver = new Driver();
        driver.setEmail("test@example.com");

        when(driverRepo.getDriverByEmail(any())).thenReturn(null);
        when(driverRepo.getDriverByNic(any())).thenReturn(null);
        when(driverRepo.getDriverByLicenseNumberNumber(any())).thenReturn(null);
        when(driverRepo.getDriverByContactNumber(any())).thenReturn(null);
        when(modelMapper.map(driverDto, Driver.class)).thenReturn(driver);
        when(driverRepo.save(any())).thenReturn(driver);

        // Act
        Driver savedDriver = driverService.saveDriver(driverDto, "Admin");

        // Assert
        assertNotNull(savedDriver);
        assertEquals("test@example.com", savedDriver.getEmail());
        verify(driverRepo, times(1)).save(any());
    }

    @Test
    void testSaveDriver_ThrowsBadCredentialsException() {
        // Arrange
        DriverDto driverDto = new DriverDto();

        // Act & Assert
        assertThrows(BadCredentialsException.class, () -> {
            driverService.saveDriver(driverDto, "User");
        });
    }

    @Test
    void testSaveDriver_ThrowsRuntimeException() {
        // Arrange
        DriverDto driverDto = new DriverDto();
        driverDto.setEmail("test@example.com");

        when(driverRepo.getDriverByEmail(any())).thenReturn(new Driver());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            driverService.saveDriver(driverDto, "Admin");
        });
    }

    @Test
    void testUpdateDriver_Success() {
        // Arrange
        DriverDto driverDto = new DriverDto();
        driverDto.setDriverId(1L);
        driverDto.setEmail("updated@example.com");

        Driver existingDriver = new Driver();
        existingDriver.setDriverId(1L);
        existingDriver.setEmail("old@example.com");

        when(driverRepo.getDriverById(1L)).thenReturn(existingDriver);

        // Mock the ModelMapper to return the updated Driver object
        Driver updatedDriver = new Driver();
        updatedDriver.setDriverId(1L);
        updatedDriver.setEmail("updated@example.com");
        when(modelMapper.map(driverDto, Driver.class)).thenReturn(updatedDriver);

        when(driverRepo.save(any())).thenReturn(updatedDriver);

        // Act
        Driver result = driverService.updateDriver(driverDto, "Admin");

        // Assert
        assertNotNull(result);
        assertEquals("updated@example.com", result.getEmail());
        verify(driverRepo, times(1)).save(any());
    }

    @Test
    void testDeleteDriver_Success() {
        // Arrange
        Driver driver = new Driver();
        driver.setDriverId(1L);

        when(driverRepo.getDriverById(1L)).thenReturn(driver);

        // Act
        Driver deletedDriver = driverService.deleteDriver(1L, "Admin");

        // Assert
        assertNotNull(deletedDriver);
        verify(driverRepo, times(1)).delete(driver);
    }

    @Test
    void testGetDriverById_Success() {
        // Arrange
        Driver driver = new Driver();
        driver.setDriverId(1L);

        when(driverRepo.getDriverById(1L)).thenReturn(driver);

        // Act
        Driver foundDriver = driverService.getDriverById(1L, "Admin");

        // Assert
        assertNotNull(foundDriver);
        assertEquals(1L, foundDriver.getDriverId());
    }

    @Test
    void testGetAllDriver_Success() {
        // Arrange
        Driver driver1 = new Driver();
        driver1.setDriverId(1L);
        Driver driver2 = new Driver();
        driver2.setDriverId(2L);

        List<Driver> drivers = Arrays.asList(driver1, driver2);
        when(driverRepo.getAllDriver()).thenReturn(drivers);

        // Mock the ModelMapper to return a list of DriverDto
        List<DriverDto> driverDtos = Arrays.asList(
                new DriverDto(),
                new DriverDto()
        );
        when(modelMapper.map(drivers, new TypeToken<List<DriverDto>>() {}.getType())).thenReturn(driverDtos);

        // Act
        List<DriverDto> result = driverService.getAllDriver("All", "Admin");

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void testGetRandomlyDriver_Success() {
        // Arrange
        Driver driver1 = new Driver();
        driver1.setDriverId(1L);
        Driver driver2 = new Driver();
        driver2.setDriverId(2L);

        List<Driver> availableDrivers = Arrays.asList(driver1, driver2);
        when(driverRepo.getAvailableDriver()).thenReturn(availableDrivers);

        // Act
        Driver randomDriver = driverService.getRandomlyDriver("User");

        // Assert
        assertNotNull(randomDriver);
        assertTrue(availableDrivers.contains(randomDriver));
    }

    @Test
    void testChangeStatusInDriver_Success() {
        // Arrange
        Driver driver = new Driver();
        driver.setDriverId(1L);
        driver.setStatus("Available");

        when(driverRepo.getDriverById(1L)).thenReturn(driver);
        when(driverRepo.save(any())).thenReturn(driver);

        // Act
        boolean result = driverService.changeStatusInDriver(1L);

        // Assert
        assertTrue(result);
        assertEquals("Busy", driver.getStatus());
    }

    @Test
    void testUpdateStatusInDriver_Success() {
        // Arrange
        Driver driver = new Driver();
        driver.setDriverId(1L);
        driver.setStatus("Busy");

        when(driverRepo.getDriverById(1L)).thenReturn(driver);
        when(driverRepo.save(any())).thenReturn(driver);

        // Act
        boolean result = driverService.updateStatusInDriver(1L);

        // Assert
        assertTrue(result);
        assertEquals("Available", driver.getStatus());
    }
}
