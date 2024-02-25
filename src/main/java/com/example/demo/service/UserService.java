package com.example.demo.service;

import com.example.demo.DataNotFoundException;
import com.example.demo.dto.UserCreateForm;
import com.example.demo.entity.SiteUser;
import com.example.demo.repository.UserRepository;
import com.example.demo.status.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    public SiteUser create(UserCreateForm userCreateForm){
        SiteUser user = new SiteUser();
        user.setUsername(userCreateForm.getUsername());
        user.setEmail(userCreateForm.getEmail());
        user.setPassword(passwordEncoder.encode(userCreateForm.getPassword1()));
        userRepository.save(user);
        return user;
    }

    public Optional<SiteUser> getUsername(String username){
        return userRepository.findByUsername(username);
    }

    public SiteUser getUser(String username){
        Optional<SiteUser> siteUser = userRepository.findByUsername(username);
        if (siteUser.isPresent()) {
            return siteUser.get();
        } else {
            throw new DataNotFoundException("siteuser not found");
        }
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<SiteUser> op = userRepository.findByUsername(username);
        if(op.isEmpty()){
            throw new UsernameNotFoundException("아이디 혹은 비밀번호가 틀렸습니다.");
        }
        SiteUser siteUser = op.get();
        //GrantedAuthority
        List<GrantedAuthority> authorities = new ArrayList<>();
        if("admin".equals(username)){
            authorities.add(new SimpleGrantedAuthority(UserRole.ADMIN.getValue()));
        }
        else{
            authorities.add(new SimpleGrantedAuthority(UserRole.USER.getValue()));
        }
        return new User(siteUser.getUsername(), siteUser.getPassword(), authorities);
    }




    /* -----------------------시큐리티가 없을 경우.-------------------------------*/
    public SiteUser notSecurityCreate(UserCreateForm userCreateForm){
        SiteUser user = new SiteUser();
        user.setUsername(userCreateForm.getUsername());
        user.setEmail(userCreateForm.getEmail());
        user.setPassword(userCreateForm.getPassword1());
        userRepository.save(user);
        return user;
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
