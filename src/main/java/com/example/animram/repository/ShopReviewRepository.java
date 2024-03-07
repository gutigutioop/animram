package com.example.animram.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.animram.entity.ShopReview;

	public interface ShopReviewRepository extends JpaRepository<ShopReview, Long>{
		
		/*@Query(value = "select avg(cast(shop_evaluate as DOUBLE PRECISION)) from shop_reviews where shop_id = ?", nativeQuery = true)
		Long selectEvaluatesAvg(Long shop_id);*/
		
		@Query(value = "select avg(cast(shop_evaluate as DOUBLE PRECISION)) from shop_reviews where shop_id = ?", nativeQuery = true)
		Double selectEvaluatesAvg(Long shop_id);
		
		@Query(value = "select shop_id,sum(shop_evaluate)from shop_reviews group by shop_id order by shop_id",nativeQuery = true)
		List<ShopReview> findAllBy();
		
		List<ShopReview> findByUserIdAndShopId(Long userId, Long shopId);
}
