package com.example.animram.controller;

import java.security.Principal;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.animram.entity.EvaluateReview;
import com.example.animram.entity.Review;
import com.example.animram.entity.UserInf;
import com.example.animram.repository.EvaluateReviewRepository;
import com.example.animram.repository.ReviewRepository;

@Controller
public class EvaluateReviewsController {

	@Autowired
	private EvaluateReviewRepository repository;
	
	@Autowired
	private ReviewRepository reviewRepository;
	
//レビューのいいね登録
	@PostMapping("/evaluate_review")
	public String create(Principal principal, @RequestParam("review_id") long reviewId, @RequestParam("sort") long sort, 
			@RequestParam("animal_type") int type) {
		Authentication authentication = (Authentication) principal;
		UserInf user = (UserInf) authentication.getPrincipal();
		Long userId = user.getUserId();

		List<EvaluateReview> results = repository.findByUserIdAndReviewId(userId, reviewId);
		if (results.size() == 0) {
			EvaluateReview entity = new EvaluateReview();
			entity.setUserId(userId);
			entity.setReviewId(reviewId);
			repository.saveAndFlush(entity);
			
			 updateEvaluateReviewCount(reviewId);
		}
		
		if (type == 1) {
			return "redirect:/animals/smile_index?sort=" + sort;
		} else if (type == 2) {
			return "redirect:/animals/angry_index?sort=" + sort;
		} else {
			return "redirect:/animals/sad_index?sort=" + sort;
		}
	}
	
	//レビューのいいね解除。
	@DeleteMapping("/evaluate_review")
	@Transactional
	public String desroy(Principal principal, @RequestParam("review_id") long reviewId, @RequestParam("sort") long sort, 
			@RequestParam("animal_type") int type) {
		Authentication authentication = (Authentication) principal;
		UserInf user = (UserInf) authentication.getPrincipal();
		Long userId = user.getUserId();
		
		List<EvaluateReview> results = repository.findByUserIdAndReviewId(userId, reviewId);
		if (results.size() == 1) {
			repository.deleteByUserIdAndReviewId(userId, reviewId);
			
			
		}
		
		updateEvaluateReviewCount(reviewId);
		
		if (type == 1) {
			return "redirect:/animals/smile_index?sort=" + sort;
		} else if (type == 2) {
			return "redirect:/animals/angry_index?sort=" + sort;
		} else {
			return "redirect:/animals/sad_index?sort=" + sort;
		}
		
	}
	 private void updateEvaluateReviewCount(long reviewId) {
         
         Review reviewEntity = reviewRepository.findById(reviewId).get();
         int u = reviewEntity.getEvaluateUsers().size();
         reviewEntity.setEvaluatereviewCount(u);
         reviewRepository.saveAndFlush(reviewEntity);
	 }
}