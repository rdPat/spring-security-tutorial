package com.springsec.tutorial.repository;

import com.springsec.tutorial.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends JpaRepository<Users,Long> {
    Users findByUsername(String username);

}
