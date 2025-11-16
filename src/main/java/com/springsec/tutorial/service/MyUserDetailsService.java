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

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;


    // -------------------------------
    // CREATE USER (sign-up)
    // -------------------------------
    public Users createUser(Users user) {

        // Check if username already exists
        Users existingUser = userRepo.findByUsername(user.getUsername());
        if (existingUser != null) {
            throw new IllegalArgumentException(
                    "User already exists with username: " + user.getUsername()
            );
        }

        // Encode password and save user
        String rawPassword = user.getPassword();
        user.setPassword(passwordEncoder.encode(rawPassword));

        Users saved = userRepo.save(user);

        // Return output with plain password (if required)
        Users output = new Users();
        output.setUserId(saved.getUserId());
        output.setUsername(saved.getUsername());
        output.setPassword(rawPassword);

        return output;
    }


    // -------------------------------
    // FETCH ALL USERS (not needed for auth)
    // -------------------------------
    public List<Users> fetchUsers() {
        return userRepo.findAll();
    }


    // -------------------------------
    // SPRING SECURITY AUTHENTICATION
    // This method is AUTOMATICALLY called by AuthenticationManager
    // -------------------------------
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        System.out.println("loadUserByUsername called for: " + username);

        Users user = userRepo.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException(
                    "User not found with username: " + username
            );
        }

        return new UserPrincipal(user);
    }


    // -------------------------------
    // EXTRA HELPER METHOD
    // -------------------------------
    public Users getUserByUsername(String username) {
        return userRepo.findByUsername(username);
    }
}
