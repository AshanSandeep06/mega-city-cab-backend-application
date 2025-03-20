package com.megacityCab.megaCityCabBackEnd.service;

import com.megacityCab.megaCityCabBackEnd.entity.User;
import com.megacityCab.megaCityCabBackEnd.repo.UserRepo;
import com.megacityCab.megaCityCabBackEnd.repo.userHasRoleRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class userDetailsService implements UserDetailsService {

    @Autowired
    UserRepo userRepo;

    @Autowired
    userHasRoleRepo userHasRoleRepo;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User userByEmail = userRepo.getUserByEmail(email);
        String password = "";
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        List<String> userRoleByUserId = userHasRoleRepo.getUserRoleByUserEmail(userByEmail.getEmail());


        if (!Objects.equals(userByEmail,null)){
            authorities.add(new SimpleGrantedAuthority(userRoleByUserId.toString()));
            password = userByEmail.getPassword();
        }

        return new org.springframework.security.core.userdetails.User(email, password, authorities);
    }

}
