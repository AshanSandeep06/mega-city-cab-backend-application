package com.megacityCab.megaCityCabBackEnd.api;

import com.megacityCab.megaCityCabBackEnd.dto.DriverDto;
import com.megacityCab.megaCityCabBackEnd.entity.Driver;
import com.megacityCab.megaCityCabBackEnd.service.DriverService;
import com.megacityCab.megaCityCabBackEnd.util.response.StandardResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/driver")
public class DriverController {

    @Autowired
    DriverService driverService;

    //    save driver endpoint
    @PostMapping(path = "/save")
    public ResponseEntity<StandardResponse> saveDriver(
            @RequestBody DriverDto dto,
            @RequestAttribute String type){
        Driver driver = driverService.saveDriver(dto, type);
        return new ResponseEntity<>(
                new StandardResponse(200,"Driver Saved",driver),
                HttpStatus.OK
        );
    }

    //    update driver endpoint
    @PutMapping(path = "/update")
    public ResponseEntity<StandardResponse> updateDriver(@RequestBody DriverDto dto,
                                                         @RequestAttribute String type){
        Driver driver = driverService.updateDriver(dto, type);
        return new ResponseEntity<>(
                new StandardResponse(200,"Driver Updated",driver),
                HttpStatus.OK
        );

    }
    //    delete driver endpoint
    @DeleteMapping(params = {"driverId"})
    public ResponseEntity<StandardResponse> deleteDriver(@RequestParam long driverId,
                                                         @RequestAttribute String type
    ){
        Driver driver = driverService.deleteDriver(driverId, type);
        return new ResponseEntity<>(
                new StandardResponse(200,"Driver Deleted",driver),
                HttpStatus.OK
        );

    }

    //    get driver by driver id endpoint
    @GetMapping(params = {"driverId"})
    public  ResponseEntity<StandardResponse> getDriverById(@RequestParam long driverId,
                                                           @RequestAttribute String type){
        Driver driver = driverService.getDriverById(driverId, type);
        return new ResponseEntity<>(
                new StandardResponse(200,"Driver Found",driver),
                HttpStatus.OK
        );

    }

    //    get driver by driver email endpoint
    @GetMapping(params = {"email"})
    public ResponseEntity<StandardResponse> getDriverByEmail(@RequestParam String email,
                                                             @RequestAttribute String type){
        Driver driverByEmail = driverService.getDriverByEmail(email, type);
        return new ResponseEntity<>(
                new StandardResponse(200,"Driver Found",driverByEmail),
                HttpStatus.OK
        );
    }

    //    get all drivers
    @GetMapping(path = "/allDrivers")
    public ResponseEntity<StandardResponse> getAllDriver(@RequestParam(value = "status", required = false, defaultValue = "all") String status, @RequestAttribute String type){
        List<DriverDto> allDriver = driverService.getAllDriver(status, type);
        return new ResponseEntity<>(
                new StandardResponse(200,"all drivers",allDriver),
                HttpStatus.OK
        );
    }
    //  get driver in randomly
    @GetMapping(path = "/getDriver")
    public ResponseEntity<StandardResponse> getRandomVehicle(@RequestAttribute String type){
        Driver randomlyDriver = driverService.getRandomlyDriver(type);
        return new ResponseEntity<>(
                new StandardResponse(200,"Driver Found",randomlyDriver),
                HttpStatus.OK
        );
    }
    //    get driver count
    @GetMapping(path = "/count")
    public ResponseEntity<StandardResponse> getDriverCount(@RequestAttribute String type){
        int driverCount = driverService.getDriverCount(type);
        return new ResponseEntity<>(
                new StandardResponse(200,"Driver Count",driverCount),
                HttpStatus.OK
        );
    }

}
