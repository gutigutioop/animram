package com.example.animram.entity;

import java.io.Serializable;

import org.springframework.stereotype.Component;


@Component
//@SessionScope
public class Session implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private Long sort;
	
	public void setSort(Long sort) {
		this.sort = sort;	
	}
	
	public Long getSort() {
		return sort;
	}
	

}
