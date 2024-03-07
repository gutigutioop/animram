package com.example.animram.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.animram.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByName(String name);
    User findByEmail(String email);

}