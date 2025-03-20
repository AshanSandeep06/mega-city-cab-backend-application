package com.megacityCab.megaCityCabBackEnd.service.impl;

import com.megacityCab.megaCityCabBackEnd.dto.AuthRequestDto;
import com.megacityCab.megaCityCabBackEnd.dto.UserDto;
import com.megacityCab.megaCityCabBackEnd.dto.UserPasswordDto;
import com.megacityCab.megaCityCabBackEnd.entity.Roles;
import com.megacityCab.megaCityCabBackEnd.entity.User;
import com.megacityCab.megaCityCabBackEnd.repo.RoleRepo;
import com.megacityCab.megaCityCabBackEnd.repo.UserRepo;
import com.megacityCab.megaCityCabBackEnd.repo.userHasRoleRepo;
import com.megacityCab.megaCityCabBackEnd.service.userHasRoleService;
import com.megacityCab.megaCityCabBackEnd.service.userService;
import com.megacityCab.megaCityCabBackEnd.util.JwtUtil;
import com.megacityCab.megaCityCabBackEnd.util.response.LoginResponse;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
//@Transactional
public class userServiceImpl implements userService {

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    UserRepo userRepo;

    @Autowired
    userHasRoleRepo userRoleDetailsRepo;

   @Autowired
   userHasRoleService userRoleService;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    RoleRepo roleRepo;

    @Autowired
    UserDetailsService userDetailsService;

    @Autowired
    JwtUtil jwtUtil;

    @Override
    public LoginResponse logUser(AuthRequestDto dto) {
        try{
            String userRole="";
            User userByEmail = userRepo.getUserByEmail(dto.getEmail());
            List<String> userRoleByUserEmail = userRoleDetailsRepo.getUserRoleByUserEmail(dto.getEmail());
            ArrayList<String> roles = new ArrayList<>();
            for (String role : userRoleByUserEmail) {
                if(role.equalsIgnoreCase("Admin")|| role.equalsIgnoreCase("User")){
                    userRole = role;
                    break;

                }
                roles.add(role);
            }
            if(Objects.equals(userByEmail,null)){
                throw new BadCredentialsException("invalid details");
            }

            LoginResponse loginResponse = new LoginResponse();
            Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userByEmail.getEmail(), dto.getPassword()));
            UserDetails userDetails = userDetailsService.loadUserByUsername(userByEmail.getEmail());

            if(userRole.equalsIgnoreCase("Admin")|| userRole.equalsIgnoreCase("User")){
                String generateToken = jwtUtil.generateToken(userDetails, userRole);
                loginResponse.setJwt(generateToken);
                loginResponse.setUserName(userByEmail.getUsername());
                loginResponse.setUserId(userByEmail.getUserId());
                loginResponse.setRole(userRole);
                loginResponse.setEmail(userByEmail.getEmail());
                loginResponse.setMessage("Login Success");
                loginResponse.setCode(200);
                return loginResponse;

            }else{
                String generateToken = jwtUtil.generateTokenRoles(userDetails, roles);
                loginResponse.setJwt(generateToken);
                loginResponse.setUserName(userByEmail.getUsername());
                loginResponse.setUserId(userByEmail.getUserId());
                loginResponse.setRole(String.valueOf(roles));
                loginResponse.setEmail(userByEmail.getEmail());
                loginResponse.setMessage("Login Success");
                loginResponse.setCode(200);
                return loginResponse;
            }


        }catch (Exception e){
            throw new BadCredentialsException("invalid details");
        }
    }

    @Override
    public LoginResponse saveUser(UserDto dto) {
        User userByEmail = userRepo.getUserByEmail(dto.getEmail());
        if(Objects.equals(userByEmail,null)) {
            User map = modelMapper.map(dto, User.class);
            map.setPassword(BCrypt.hashpw(dto.getPassword(), BCrypt.gensalt(10)));
            User newUser = userRepo.save(map);

            // userRepo.flush();

            Roles byRole = roleRepo.findByRole(dto.getRole());

            LoginResponse loginResponse = new LoginResponse();

            if (!Objects.equals(byRole, null)) {
                userRoleService.saveUserRoleDetails(newUser.getUserId(), byRole.getUserRoleId());

                Authentication authenticate =
                        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(newUser.getEmail(), dto.getPassword()));

                UserDetails userDetails = userDetailsService.loadUserByUsername(newUser.getEmail());
                String generateToken = jwtUtil.generateToken(userDetails, dto.getRole());

                loginResponse.setMessage("User Created Successfully");
                loginResponse.setJwt(generateToken);
                loginResponse.setUserName(newUser.getUsername());
                loginResponse.setUserId(newUser.getUserId());
                loginResponse.setEmail(newUser.getEmail());
                loginResponse.setRole(dto.getRole());
                loginResponse.setCode(200);
                return loginResponse;
            }
        }
        throw new BadCredentialsException("User Already Exist");
    }

    @Override
    public User updateUser(UserDto dto, String type) {
        if (!type.equals("User") && !type.equals("Admin")){
            throw new BadCredentialsException("dont have permission");
        }

        System.out.println("user id: "+dto.getId());

        User userById = userRepo.getUserById(dto.getId());

        System.out.println("userById: "+userById);

        if(!Objects.equals(userById,null)){
            User map = modelMapper.map(dto, User.class);
            map.setStatus("1");
            map.setPassword(userById.getPassword());
            return  userRepo.save(map);

        }
        throw new RuntimeException("user not Exist!");
    }

    @Override
    public User deleteUser(long userId, String type) {
        if (!type.equals("User") && !type.equals("Admin")){
            throw new BadCredentialsException("dont have permission");
        }
        User userById = userRepo.getUserById(userId);
        if(!Objects.equals(userById,null)){
            userRoleService.deleteUserHasRole(userId);
            userRepo.delete(userById);
            return userById;

        }
        throw new RuntimeException("user id is invalid");
    }

    @Override
    public List<UserDto> getAllUser(String type) {
        if (!type.equals("User") && !type.equals("Admin")){
            throw new BadCredentialsException("dont have permission");
        }
        return modelMapper.map(userRepo.findAll(),new TypeToken<List<UserDto>>() {}.getType());
    }

    @Override
    public int getUserCount(String type) {
        if ( !type.equals("Admin")){
            throw new BadCredentialsException("dont have permission");
        }
        return userRepo.getTotalUser();
    }

    @Override
    public String updateUserPassword(UserPasswordDto dto, String type) {
        if (!type.equals("User") && !type.equals("Admin")){
            throw new BadCredentialsException("dont have permission");

        }
        User userById = userRepo.getUserById(dto.getUserId());
        if(!Objects.equals(userById,null)){
            boolean checkpw = BCrypt.checkpw(dto.getCurrentPassword(), userById.getPassword());
            if(checkpw){
                userById.setPassword(BCrypt.hashpw(dto.getNewPassword(), BCrypt.gensalt(10)));
                return "Password Updated Successfully";
            }
            throw new RuntimeException("user current password is wrong!");
        }
        throw new RuntimeException("user is not exist");
    }

    @Override
    public User getUserByUserId(long userId, String type) {
//        if (!type.equals("User")){
//            throw new BadCredentialsException("dont have permission");
//        }
        User userById = userRepo.getUserById(userId);
        if (!Objects.equals(userById,null)){
            return userById;
        }
        throw new RuntimeException("user not exit");
    }


}
