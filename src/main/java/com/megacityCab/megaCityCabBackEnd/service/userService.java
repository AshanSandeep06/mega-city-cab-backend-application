package com.megacityCab.megaCityCabBackEnd.service;

import com.megacityCab.megaCityCabBackEnd.dto.AuthRequestDto;
import com.megacityCab.megaCityCabBackEnd.dto.UserDto;
import com.megacityCab.megaCityCabBackEnd.dto.UserPasswordDto;
import com.megacityCab.megaCityCabBackEnd.entity.User;
import com.megacityCab.megaCityCabBackEnd.util.response.LoginResponse;

import java.util.List;

public interface userService {
    LoginResponse logUser(AuthRequestDto dto);
    LoginResponse saveUser(UserDto dto);
    User updateUser(UserDto dto, String type);
    User deleteUser(long userId,String type);
    List<UserDto> getAllUser(String type);
    int getUserCount(String type);
    String updateUserPassword(UserPasswordDto dto, String type);
    User getUserByUserId(long userId, String type);
}
