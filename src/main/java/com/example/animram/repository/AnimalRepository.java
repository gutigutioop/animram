package com.example.animram.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.animram.entity.Animal;





	public interface AnimalRepository extends JpaRepository<Animal, Long> {

	    
	    //一覧表示古い順
	    List<Animal> findByTypeOrderByUpdatedAtDesc(int type);   
	    
	    //一覧表示新着順
	    List<Animal> findByTypeOrderByUpdatedAtAsc(int type);  
	    
	    //一覧表示人気順
	   List<Animal> findByTypeOrderByEvaluateCountDesc(int type);
	    
	    //一覧表示レビュー数順
	    List<Animal> findByTypeOrderByReviewCountDesc(int type);
	    
	    List<Animal>findTop3ByOrderByEvaluateCountDesc();
	}

