package com.megacityCab.megaCityCabBackEnd.api;

import com.megacityCab.megaCityCabBackEnd.dto.AuthRequestDto;
import com.megacityCab.megaCityCabBackEnd.dto.UserDto;
import com.megacityCab.megaCityCabBackEnd.dto.UserPasswordDto;
import com.megacityCab.megaCityCabBackEnd.entity.User;
import com.megacityCab.megaCityCabBackEnd.service.userService;
import com.megacityCab.megaCityCabBackEnd.util.response.LoginResponse;
import com.megacityCab.megaCityCabBackEnd.util.response.StandardResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
@CrossOrigin(origins = "*",allowedHeaders = "*")
public class userController {

    @Autowired
    userService service;

    //    user or admin login
    @PostMapping(path = "/login")
    public ResponseEntity<LoginResponse> userLogin(@RequestBody AuthRequestDto dto) {
        LoginResponse response = service.logUser(dto);
        return new ResponseEntity<LoginResponse>(
                response,
                HttpStatus.OK
        );
    }

    //    user register
    @PostMapping(path = "/register")
    public ResponseEntity<?> saveUser(@RequestBody UserDto dto){
        LoginResponse response = service.saveUser(dto);
        return new ResponseEntity<>(
                response,
                HttpStatus.CREATED
        );
    }

    //    delete user
    @DeleteMapping(params = {"userId"})
    public ResponseEntity<StandardResponse> deleteUser(@RequestParam Long userId,
                                                       @RequestAttribute String type){
        User user = service.deleteUser(userId,type);
        return new ResponseEntity<>(
                new StandardResponse(200,"User Deleted",user),
                HttpStatus.OK
        );
    }

    //    update user
    @PutMapping(path = "/update")
    public ResponseEntity<StandardResponse> updateUser(@RequestBody UserDto dto,
                                                       @RequestAttribute String type){
        User user = service.updateUser(dto, type);
        return new ResponseEntity<>(
                new StandardResponse(200,"User Updated",user),
                HttpStatus.OK
        );
    }
    //    get all users
    @GetMapping(path = "/allUsers")
    public ResponseEntity<StandardResponse> getAllUser(@RequestAttribute String type){
        List<UserDto> allUser =service.getAllUser(type);
        return new ResponseEntity<>(
                new StandardResponse(200,"all users",allUser),
                HttpStatus.OK
        );
    }
    @GetMapping(path = "/count")
    public ResponseEntity<StandardResponse> countUser(@RequestAttribute String type){
        int count = service.getUserCount(type);
        return new ResponseEntity<>(
                new StandardResponse(200,"User Count",count),
                HttpStatus.OK
        );
    }

    @PutMapping(path = "/updateUserPassword")
    public ResponseEntity<StandardResponse> updateUserPassword(@RequestBody UserPasswordDto dto,
                                                               @RequestAttribute String type){
        String response = service.updateUserPassword(dto, type);
        return new ResponseEntity<>(
                new StandardResponse(200,"Password Updated",response),
                HttpStatus.OK
        );
    }

    @GetMapping(params = {"userId"})
    ResponseEntity<StandardResponse> getUserById(@RequestParam long userId,
                                                 @RequestAttribute String type){
        User userByUserId = service.getUserByUserId(userId, type);
        return new ResponseEntity<>(
                new StandardResponse(200,"get user",userByUserId),
                HttpStatus.OK
        );
    }
}
