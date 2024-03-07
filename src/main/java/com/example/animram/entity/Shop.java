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
@Table(name = "shops")
@Data
public class Shop extends AbstractEntity implements Serializable  {
	private static final long serialVersionUID = 1L;
	
	@Id
	@SequenceGenerator(name = "shop_id_seq")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	@Column
	private Long userId;
	
	@Column
	private String  shopPath;
	
	@Column 
	private Double latitude;
	
	@Column
	private Double longitude;
	
	@Column
	private String address;
	
	@Column
	private String access;
	
	@Column
	private int money;
	
	@Column
	private String shopDescription;
	
	@ManyToOne
	@JoinColumn(name = "userId", insertable = false, updatable = false)
    private User user;
	
	 @OneToMany
	    @JoinColumn(name = "shopId", insertable = false, updatable = false)
	    private List<ShopReview> ShopReviews;
	
}
