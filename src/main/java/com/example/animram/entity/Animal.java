package com.example.animram.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Data;

@Entity
	@Table(name = "animals")
	@Data
	public class Animal extends AbstractEntity implements Serializable {
	    private static final long serialVersionUID = 1L;

	    @Id
	    @SequenceGenerator(name = "animal_id_seq")
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;
	    
	    //投稿タイプ笑顔・怒顔・悲顔分ける。
	    @Column(nullable = false)
	    private Integer type;
	    
	    //人気順に一覧を並び替える時に使用。
	    @Column(nullable = false)
	    private Integer evaluateCount;
	    
	    //レビューの多い順に一覧を並び替える時に使用。
	    @Column(nullable = false)
	    private Integer reviewCount;

	    @Column(nullable = false)
	    private Long userId;

	    @Column(nullable = false)
	    private String path;

	    @Column(nullable = false, length = 1000)
	    private String description;

	    @Column
	    private Double latitude;

	    @Column
	    private Double longitude;

	    @ManyToOne
	    @JoinColumn(name = "userId", insertable = false, updatable = false)
	    private User user;
	    
	   @OneToMany
	    @JoinColumn(name = "animalId", insertable = false, updatable = false)
	    private List<Evaluate> evaluates;
	    
	    @OneToMany
	    @JoinColumn(name = "animalId", insertable = false, updatable = false)
	    private List<Review> reviews;

	}