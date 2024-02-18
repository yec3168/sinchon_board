package com.example.demo.service;

import com.example.demo.dto.UserCreateForm;
import com.example.demo.entity.SiteUser;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    public SiteUser create(String username, String password, String email){
        SiteUser user = new SiteUser();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
        return user;
    }


    /* 시큐리티가 없을 경우.*/
    public SiteUser notSecurityCreate(UserCreateForm userCreateForm){
        SiteUser user = new SiteUser();
        user.setUsername(userCreateForm.getUsername());
        user.setEmail(userCreateForm.getEmail());
        user.setPassword(userCreateForm.getPassword1());
        userRepository.save(user);
        return user;
    }

    public Optional<SiteUser> getUsername(String username){
        return userRepository.findByUsername(username);
    }

    public SiteUser noSecurityLogin(String username, String password){
        Optional<SiteUser> op = userRepository.findByUsername(username);
        if(op.isPresent()){
            SiteUser user = op.get();
            if(user.getPassword().equals(password))
                return user;
            else
                return null;
        }
        return null;
    }
}
