package com.example.animram.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.animram.entity.Shop;



public interface ShopRepository extends JpaRepository<Shop, Long>  {
	
	//List <Shop> findByAddressLike(String address);
	
	public Page <Shop> findByAddressLike(String address,Pageable pageable);
	
	public Page<Shop> findAll(Pageable pageable);

}
