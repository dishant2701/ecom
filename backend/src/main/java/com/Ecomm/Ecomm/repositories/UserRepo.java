package com.Ecomm.Ecomm.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Ecomm.Ecomm.models.User;

public interface UserRepo extends JpaRepository<User, Long> {

    User findFirstByUsername(String username);

    User findByUsername(String username);

    Boolean existsByUsername(String username);

}
