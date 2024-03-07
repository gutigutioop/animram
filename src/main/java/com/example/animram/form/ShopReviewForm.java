package com.example.animram.form;

import lombok.Data;

@Data
public class ShopReviewForm {
	
	private Long id;
	
	private Long userId;
	
	private Long shopId;
	
	private String shopReview;
	
	private Double shopEvaluate;
	
	
}
