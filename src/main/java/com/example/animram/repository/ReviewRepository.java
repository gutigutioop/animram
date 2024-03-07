package com.example.animram.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.animram.entity.Review;

public interface ReviewRepository extends JpaRepository<Review, Long> {
	// List<Review> findAll(Long Id);
	 public List<Review> findByUserIdAndId(Long userId, Long id);
	 
	 //メソッド一つ追加。
	List<Review> findTop3ByAnimalIdOrderByEvaluatereviewCountDesc(Long animalId);
	 
	 
	}
