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
	@Table(name = "evaluate_reviews")
	@Data
	public class EvaluateReview  extends AbstractEntity implements Serializable {
	    private static final long serialVersionUID = 1L;

	    @Id
	    @SequenceGenerator(name = "evaluate_review_id_seq")
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;

	    @Column(nullable = false)
	    private Long userId;

	    @Column(nullable = false)
	    private Long reviewId;

	   
	    
	    /*@ManyToOne
	   * @JoinColumn(name = "reviewId", insertable = false, updatable = false)
	    *private Review review;
	    *
	    *private List< Review> reviews;
	    */
	}

