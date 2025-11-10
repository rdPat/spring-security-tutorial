package com.springsec.tutorial.controller;

import com.springsec.tutorial.dto.FetchUsersResponseDto;
import com.springsec.tutorial.model.Users;
import com.springsec.tutorial.repository.UserRepo;
import com.springsec.tutorial.service.MyUserDetailsService;
import jakarta.persistence.GeneratedValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class UsersController
{
    @Autowired
    private MyUserDetailsService myUserDetailsService;

    @PostMapping("/create/user")
    public ResponseEntity<Users> createUser(@RequestBody Users user)
    {
        myUserDetailsService.createUser(user);

        return ResponseEntity.ok(user);
    }

    @GetMapping("/fetch/users")
    public ResponseEntity<List<FetchUsersResponseDto>> getAllUsers()
    {
        List<Users> usersList=myUserDetailsService.fetchUsers();

        List<FetchUsersResponseDto> responseDtos=usersList.stream().map(l->new FetchUsersResponseDto(l.getUsername(),l.getUserId()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(responseDtos);
    }

}
