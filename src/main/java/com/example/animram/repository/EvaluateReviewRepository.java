package com.example.animram.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.animram.entity.EvaluateReview;

public interface EvaluateReviewRepository extends JpaRepository<EvaluateReview, Long> {

	    public List<EvaluateReview> findByUserIdAndReviewId(Long userId, Long reviewId);

	    public void deleteByUserIdAndReviewId(Long userId, Long reviewId);
	    
	   // @Query(value = "select  count(*) review_id from evaluate_reviews group by review_id order by review_id desc", nativeQuery = true)
	    //public List<EvaluateReview> findByAll();
	}
