package com.megacityCab.megaCityCabBackEnd.api;

import com.megacityCab.megaCityCabBackEnd.dto.VehicleDto;
import com.megacityCab.megaCityCabBackEnd.entity.CustomQuery.VehiclecategoryCustomResult;
import com.megacityCab.megaCityCabBackEnd.entity.Vehicle;
import com.megacityCab.megaCityCabBackEnd.entity.custom.VehicleCustomResult;
import com.megacityCab.megaCityCabBackEnd.service.VehicleService;
import com.megacityCab.megaCityCabBackEnd.util.response.StandardResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/vehicle")
@CrossOrigin(origins = "*",allowedHeaders = "*")
public class VehicleController {

    @Autowired
    VehicleService vehicleService;

    //    save vehicle
    @PostMapping(path = "/save")
    public ResponseEntity<StandardResponse> saveVehicle(
            @RequestPart("dto") VehicleDto dto,
            @RequestPart("imageFile") MultipartFile imageFile,
            @RequestAttribute String type
    ) {
        Vehicle vehicle = vehicleService.saveVehicle(dto, imageFile, type);
        return new ResponseEntity<>(
                new StandardResponse(200, "vehicle saved successfully", vehicle),
                HttpStatus.CREATED
        );
    }

    //    update vehicle
    @PutMapping(path = "/update")
    public ResponseEntity<StandardResponse> updateVehicle(
            @RequestPart("dto") VehicleDto dto,
            @RequestPart("imageFile") MultipartFile imageFile,
            @RequestAttribute String type
    ) {
        Vehicle vehicle = vehicleService.updateVehicle(dto, imageFile, type);
        return new ResponseEntity<>(
                new StandardResponse(200, "Vehicle Updated", vehicle),
                HttpStatus.OK
        );
    }

    //    get all vehicles with category
    @GetMapping(path = "allVehicales/with/category")
    public ResponseEntity<StandardResponse> getAllVehiclesWithCategory(@RequestParam(value = "status", required = false, defaultValue = "all") String status, @RequestAttribute String type) {
        List<VehicleCustomResult> allVehiclesWithCategory = vehicleService.getAllVehiclesWithCategory(status, type);
        return new ResponseEntity<>(
                new StandardResponse(200, "all vehicles", allVehiclesWithCategory),
                HttpStatus.OK
        );
    }

    //    delete vehicle
    @DeleteMapping(params = {"vehicleId"})
    public ResponseEntity<StandardResponse> deleteVehicle(@RequestParam Long vehicleId,
                                                          @RequestAttribute String type) {
        Vehicle vehicle = vehicleService.deleteVehicle(vehicleId, type);
        return new ResponseEntity<>(
                new StandardResponse(200, "Vehicle Deleted", vehicle),
                HttpStatus.OK
        );
    }

    //    get vehicle model by category
    @GetMapping(params = {"categoryName"})
    public ResponseEntity<StandardResponse> getVehicleModelByCategory(@RequestParam String categoryName, @RequestAttribute String type) {
        List<String> vehicleModelByCategoryName = vehicleService.getVehicleModelByCategoryName(categoryName, type);
        return new ResponseEntity<>(
                new StandardResponse(200, "Vehicle Model Found", vehicleModelByCategoryName),
                HttpStatus.OK
        );
    }

    //    all vehicle count
    @GetMapping(path = "/count")
    public ResponseEntity<StandardResponse> getAllVehicleCount(@RequestAttribute String type) {
        int vehicleCount = vehicleService.getVehicleCount(type);
        return new ResponseEntity<>(
                new StandardResponse(200, "Vehicle Count", vehicleCount),
                HttpStatus.OK
        );
    }

    //    get available vehicle in random
    @GetMapping(params = {"model"})
    public ResponseEntity<StandardResponse> getRandomVehicle(@RequestParam String model, @RequestAttribute String type) {
        Vehicle vehicle = vehicleService.randomlyGetVehicle(model, type);
        return new ResponseEntity<>(
                new StandardResponse(200, "Vehicle Found", vehicle),
                HttpStatus.OK
        );
    }

    //     get all vehicle model
    @GetMapping(path = "/model")
    public ResponseEntity<StandardResponse> getAllVehicleModel(@RequestAttribute String type) {
        List<String> allVehicleModel = vehicleService.getAllVehicleModel(type);
        return new ResponseEntity<>(
                new StandardResponse(200, "get all vehicle models", allVehicleModel),
                HttpStatus.OK
        );
    }
}
