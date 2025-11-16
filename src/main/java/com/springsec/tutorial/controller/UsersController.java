package com.springsec.tutorial.controller;

import com.springsec.tutorial.dto.FetchUsersResponseDto;
import com.springsec.tutorial.model.UserPrincipal;
import com.springsec.tutorial.model.Users;
import com.springsec.tutorial.repository.UserRepo;
import com.springsec.tutorial.service.MyUserDetailsService;
import com.springsec.tutorial.service.ServicesWithJwt;
import jakarta.persistence.GeneratedValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class UsersController
{
    @Autowired
    private MyUserDetailsService myUserDetailsService;

    @Autowired
    private ServicesWithJwt servicesWithJwt;

    @PostMapping("/create/user")
    public ResponseEntity<Users> createUser(@RequestBody Users user)
    {
        Users output=myUserDetailsService.createUser(user);

        return ResponseEntity.ok(output);
    }

    @GetMapping("/fetch/users")
    public ResponseEntity<List<FetchUsersResponseDto>> getAllUsers()
    {
        List<Users> usersList=myUserDetailsService.fetchUsers();

        List<FetchUsersResponseDto> responseDtos=usersList.stream().map(l->new FetchUsersResponseDto(l.getUsername(),l.getUserId()))
                .collect(Collectors.toList());
        System.out.println(responseDtos);
        return ResponseEntity.ok(responseDtos);
    }

    @GetMapping("/get/user/{username}")
    public ResponseEntity<Users> getUserById(@PathVariable String username)
    {
        UserPrincipal output= (UserPrincipal) myUserDetailsService.loadUserByUsername(username);
        Users o=new Users();
        o.setUserId(output.getUserId());
        o.setUsername(output.getUsername());
        o.setPassword(output.getPassword());
        System.out.println(o.getPassword());

        return ResponseEntity.ok(o);
    }

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/user/present/{username}/{password}")
    public ResponseEntity<Users> verfiyUserPresentOrNot(@PathVariable String username,
                                                        @PathVariable String password)
    {
        Users user_indb= myUserDetailsService.getUserByUsername(username);
        if (user_indb == null) {
           throw  new UsernameNotFoundException("User  not in DB .............");
        }
        boolean isPasswordMatch = passwordEncoder.matches(password, user_indb.getPassword());


        System.out.println(isPasswordMatch);

        if(isPasswordMatch)
        {
            return ResponseEntity.ok(user_indb);
        }
        return null;
    }

    @PostMapping("/login")
    public String loginWithJWT(@RequestBody Users user)
    {

        return servicesWithJwt.verifyByJWT(user);

    }



}
