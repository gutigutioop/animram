package com.example.animram.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "shop_reviews")
@Data
public class ShopReview extends AbstractEntity implements Serializable  {
	private static final long serialVersionUID = 1L;
	
	@Id
	@SequenceGenerator(name = "shop_revew_id_seq")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column
	private Long userId;
	
	@Column
	private Long shopId;
	
	@Column
	private String shopReview;
	
	@Column
	private Double shopEvaluate;
	
	

}
