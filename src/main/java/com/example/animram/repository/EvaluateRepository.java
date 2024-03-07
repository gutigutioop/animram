package com.example.animram.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.animram.entity.Evaluate;

public interface EvaluateRepository extends JpaRepository<Evaluate, Long> {

	    public List<Evaluate> findByUserIdAndAnimalId(Long userId, Long animalId);
	    
	    public List<Evaluate> findAll();
	    
	    //public List<Evaluate> findByUserIdAndAnimalIdAndAnimal(Long userId, Long animalId, type);

	    //public List<Evaluate> findByUserIdAndType(Long userId, int type);
	    
	    public void deleteByUserIdAndAnimalId(Long userId, Long animalId);
	}
