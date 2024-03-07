package com.example.animram.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.animram.entity.Shop;
import com.example.animram.repository.ShopRepository;

@Service
public class ShopService {

	@Autowired
	ShopRepository repository;
	
	public Page<Shop> getShops(Pageable Pageable){
		return repository.findAll(Pageable);
	}
	
	public Page<Shop> getShopNarrowDown(String address,Pageable pageable){
		return repository.findByAddressLike(address, pageable);
	}
	
}
