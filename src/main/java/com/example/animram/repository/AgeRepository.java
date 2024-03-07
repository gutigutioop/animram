package com.example.animram.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.animram.entity.Age;

@Repository
public interface AgeRepository extends JpaRepository<Age, Long> {
}
