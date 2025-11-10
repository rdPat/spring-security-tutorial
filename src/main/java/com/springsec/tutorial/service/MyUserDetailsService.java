package com.springsec.tutorial.service;

import com.springsec.tutorial.model.UserPrincipal;
import com.springsec.tutorial.model.Users;
import com.springsec.tutorial.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Users createUser(Users user)
    {
        Users existingUser = userRepo.findByUsername(user.getUsername());
        if (existingUser != null) {
            throw new UsernameNotFoundException("User already exists with username: " + user.getUsername());
        }

        user.setPassword(passwordEncoder.encode(user.getPassword())); // encode plain password
        return userRepo.save(user); // return saved user

    }

    public List<Users> fetchUsers()
    {
        List<Users> userList=userRepo.findAll();
        return userList;

    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
       Users user=userRepo.findByUsername(username);

       if(user==null)
       {
           System.out.println("User Not Found.......");
       }
       return new UserPrincipal(user);
    }
}
