package com.mega.city.cab.backend.service;

import com.megacityCab.megaCityCabBackEnd.dto.VehicleDto;
import com.megacityCab.megaCityCabBackEnd.entity.Vehicle;
import com.megacityCab.megaCityCabBackEnd.entity.custom.VehicleCustomResult;
import com.megacityCab.megaCityCabBackEnd.repo.VehicleRepo;
import com.megacityCab.megaCityCabBackEnd.service.VehicleCategoryDetailsService;
import com.megacityCab.megaCityCabBackEnd.service.VehicleCategoryService;
import com.megacityCab.megaCityCabBackEnd.service.impl.VehicleServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class VehicleServiceImplTest {

    @InjectMocks
    private VehicleServiceImpl vehicleService;

    @Mock
    private VehicleRepo vehicleRepo;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private VehicleCategoryService categoryService;

    @Mock
    private VehicleCategoryDetailsService categoryDetailsService;

    @Mock
    private MultipartFile file;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void saveVehicle_ShouldThrowException_WhenUserIsNotAdmin() {
        VehicleDto vehicleDto = new VehicleDto();
        String type = "User";

        assertThrows(BadCredentialsException.class, () -> {
            vehicleService.saveVehicle(vehicleDto, file, type);
        });
    }

    @Test
    void saveVehicle_ShouldSaveVehicle_WhenUserIsAdmin() throws IOException {
        VehicleDto vehicleDto = new VehicleDto();
        vehicleDto.setPlateNumber("ABC123");
        vehicleDto.setCategory("Sedan");

        Vehicle vehicle = new Vehicle();
        vehicle.setPlateNumber("ABC123");
        vehicle.setVehicleId(1L); // Set a non-null vehicleId

        when(vehicleRepo.findByPlateNumber(anyString())).thenReturn(null);
        when(modelMapper.map(any(VehicleDto.class), eq(Vehicle.class))).thenReturn(vehicle);
        when(file.getBytes()).thenReturn(new byte[0]);
        when(vehicleRepo.save(any(Vehicle.class))).thenReturn(vehicle);

        String type = "Admin";
        Vehicle savedVehicle = vehicleService.saveVehicle(vehicleDto, file, type);

        assertNotNull(savedVehicle);
        assertEquals("ABC123", savedVehicle.getPlateNumber());
        assertEquals(1L, savedVehicle.getVehicleId()); // Ensure vehicleId is set
        verify(vehicleRepo, times(1)).save(any(Vehicle.class));
        verify(categoryService, times(1)).saveCategory(eq("Sedan"), eq(1L)); // Verify categoryService is called with the correct vehicleId
    }

    @Test
    void updateVehicle_ShouldThrowException_WhenUserIsNotAdmin() {
        VehicleDto vehicleDto = new VehicleDto();
        String type = "User";

        assertThrows(BadCredentialsException.class, () -> {
            vehicleService.updateVehicle(vehicleDto, file, type);
        });
    }

    @Test
    void updateVehicle_ShouldUpdateVehicle_WhenUserIsAdmin() throws IOException {
        VehicleDto vehicleDto = new VehicleDto();
        vehicleDto.setVehicleId(1L);
        vehicleDto.setPlateNumber("ABC123");
        vehicleDto.setCategory("Sedan");

        Vehicle existingVehicle = new Vehicle();
        existingVehicle.setVehicleId(1L);
        existingVehicle.setPlateNumber("ABC123");

        when(vehicleRepo.findByVehicleId(anyLong())).thenReturn(existingVehicle);
        when(modelMapper.map(any(VehicleDto.class), eq(Vehicle.class))).thenReturn(existingVehicle);
        when(file.getBytes()).thenReturn(new byte[0]);
        when(vehicleRepo.save(any(Vehicle.class))).thenReturn(existingVehicle);

        String type = "Admin";
        Vehicle updatedVehicle = vehicleService.updateVehicle(vehicleDto, file, type);

        assertNotNull(updatedVehicle);
        assertEquals(1L, updatedVehicle.getVehicleId());
        verify(vehicleRepo, times(1)).save(any(Vehicle.class));
    }

    @Test
    void deleteVehicle_ShouldThrowException_WhenUserIsNotAdmin() {
        long vehicleId = 1L;
        String type = "User";

        assertThrows(BadCredentialsException.class, () -> {
            vehicleService.deleteVehicle(vehicleId, type);
        });
    }

    @Test
    void deleteVehicle_ShouldDeleteVehicle_WhenUserIsAdmin() {
        long vehicleId = 1L;
        String type = "Admin";

        Vehicle vehicle = new Vehicle();
        vehicle.setVehicleId(vehicleId);

        when(vehicleRepo.findByVehicleId(anyLong())).thenReturn(vehicle);
        when(categoryDetailsService.deleteCategoryDetails(anyLong())).thenReturn(true);

        Vehicle deletedVehicle = vehicleService.deleteVehicle(vehicleId, type);

        assertNotNull(deletedVehicle);
        assertEquals(1L, deletedVehicle.getVehicleId());
        verify(vehicleRepo, times(1)).delete(any(Vehicle.class));
    }

    @Test
    void getAllVehiclesWithCategory_ShouldThrowException_WhenUserIsNotAdmin() {
        String type = "User";
        String status = "All";

        assertThrows(BadCredentialsException.class, () -> {
            vehicleService.getAllVehiclesWithCategory(status, type);
        });
    }

    @Test
    void getAllVehiclesWithCategory_ShouldReturnList_WhenUserIsAdmin() {
        String type = "Admin";
        List<VehicleCustomResult> vehicles = new ArrayList<>();
        vehicles.add(new VehicleCustomResult() {
            @Override
            public long getVehicleId() {
                return 0;
            }

            @Override
            public String getModel() {
                return "";
            }

            @Override
            public String getPlateNumber() {
                return "";
            }

            @Override
            public String getCategory() {
                return "";
            }

            @Override
            public double getPricePerKm() {
                return 0;
            }

            @Override
            public int getPassengerCount() {
                return 0;
            }

            @Override
            public String getImage() {
                return "";
            }

            @Override
            public String getVehicleStatus() {
                return null;
            }

            @Override
            public String getCategoryStatus() {
                return null;
            }
        });

        when(vehicleRepo.getAllVehiclesWithCategory()).thenReturn(vehicles);

        List<VehicleCustomResult> result = vehicleService.getAllVehiclesWithCategory("All", type);

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void getVehicleModelByCategoryName_ShouldThrowException_WhenUserIsNotAuthorized() {
        String categoryName = "Sedan";
        String type = "Guest";

        assertThrows(BadCredentialsException.class, () -> {
            vehicleService.getVehicleModelByCategoryName(categoryName, type);
        });
    }

    @Test
    void getVehicleModelByCategoryName_ShouldReturnList_WhenUserIsAuthorized() {
        String categoryName = "Sedan";
        String type = "User";
        List<String> models = Arrays.asList("Model1", "Model2");

        when(vehicleRepo.getVehicleModelByCategoryName(anyString())).thenReturn(models);

        List<String> result = vehicleService.getVehicleModelByCategoryName(categoryName, type);

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void getVehicleCount_ShouldThrowException_WhenUserIsNotAuthorized() {
        String type = "Guest";

        assertThrows(BadCredentialsException.class, () -> {
            vehicleService.getVehicleCount(type);
        });
    }

    @Test
    void getVehicleCount_ShouldReturnCount_WhenUserIsAuthorized() {
        String type = "User";
        int count = 10;

        when(vehicleRepo.getVehicleCount()).thenReturn(count);

        int result = vehicleService.getVehicleCount(type);

        assertEquals(10, result);
    }

    @Test
    void randomlyGetVehicle_ShouldThrowException_WhenUserIsNotAuthorized() {
        String modelName = "Model1";
        String type = "Guest";

        assertThrows(BadCredentialsException.class, () -> {
            vehicleService.randomlyGetVehicle(modelName, type);
        });
    }

    @Test
    void randomlyGetVehicle_ShouldReturnVehicle_WhenUserIsAuthorized() {
        String modelName = "Model1";
        String type = "User";
        List<Vehicle> vehicles = Arrays.asList(new Vehicle(), new Vehicle());

        when(vehicleRepo.findByVehicleModel(anyString())).thenReturn(vehicles);

        Vehicle result = vehicleService.randomlyGetVehicle(modelName, type);

        assertNotNull(result);
    }

    @Test
    void changeVehicleStatus_ShouldReturnTrue_WhenStatusIsAvailable() {
        long vehicleId = 1L;
        Vehicle vehicle = new Vehicle();
        vehicle.setVehicleId(vehicleId);
        vehicle.setStatus("Available");

        when(vehicleRepo.findByVehicleId(anyLong())).thenReturn(vehicle);

        boolean result = vehicleService.changeVehicleStatus(vehicleId);

        assertTrue(result);
        assertEquals("Booking", vehicle.getStatus());
        verify(vehicleRepo, times(1)).save(any(Vehicle.class));
    }

    @Test
    void updateVehicleStatus_ShouldReturnTrue_WhenStatusIsBooking() {
        long vehicleId = 1L;
        Vehicle vehicle = new Vehicle();
        vehicle.setVehicleId(vehicleId);
        vehicle.setStatus("Booking");

        when(vehicleRepo.findByVehicleId(anyLong())).thenReturn(vehicle);

        boolean result = vehicleService.updateVehicleStatus(vehicleId);

        assertTrue(result);
        assertEquals("Available", vehicle.getStatus());
        verify(vehicleRepo, times(1)).save(any(Vehicle.class));
    }

    @Test
    void getAllVehicleModel_ShouldThrowException_WhenUserIsNotAuthorized() {
        String type = "Guest";

        assertThrows(BadCredentialsException.class, () -> {
            vehicleService.getAllVehicleModel(type);
        });
    }

    @Test
    void getAllVehicleModel_ShouldReturnList_WhenUserIsAuthorized() {
        String type = "User";
        List<String> models = Arrays.asList("Model1", "Model2");

        when(vehicleRepo.getAllVehicleModel()).thenReturn(models);

        List<String> result = vehicleService.getAllVehicleModel(type);

        assertNotNull(result);
        assertEquals(2, result.size());
    }
}
