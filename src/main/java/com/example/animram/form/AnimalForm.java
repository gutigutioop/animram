package com.example.animram.form;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.web.multipart.MultipartFile;

import com.example.animram.entity.Review;
import com.example.animram.entity.User;
import com.example.animram.validation.constraints.ImageByte;
import com.example.animram.validation.constraints.ImageNotEmpty;

import lombok.Data;

@Data
	public class AnimalForm {

	    private Long id;
	    
	    private Integer type;

	    private Long userId;
	    
	    //投稿された写真data。
	    @ImageNotEmpty
	    @ImageByte(max = 2000000)
	    private MultipartFile image;
	    
	    //クライアントに写真が見れるようにエンコード。
	    private String imageData;
	    
	    //写真の保存場所。
	    private String path;
	    
	    //写真に対する投稿者の投稿。
	    @NotEmpty
	    @Size(max = 1000)
	    private String description;
	    
	    private Double latitude;

	    private Double longitude;

	    private UserForm user;
	    
	    /*新規投稿時に"hidden”で初期値０をDBに保存する時使用した。
	     * private Integer evaluateCount;
	     *private Integer reviewCount;
	    */
	    
	    //写真のいいね総数を数えるために必要。
        private List<EvaluateForm> evaluates;
        
        //レビューに関する情報が入ってる。レビューに対していいねした人のリスト・ログインユーザーがいいねしたか確かめるフラグも入ってる。
        private List<ReviewForm> reviews;
	    
       /* private List<EvaluateReviewForm> evaluate_reviews;レビューの中にはいってるから不要になった。
	       private EvaluateReviewForm evaluate_review;レビューの中にはいってるから不要になった。
	   */
	    
	    //写真に対していいねしたユーザーのリスト。
	    private List<User> evaluateUsers;
	    
	  //写真に対して自分がいいねしたかみる為のフラグ。初期地はfalse
	    private boolean  evaluatedByMe;
	    
	    //レビュートップ３をhtml表示させる為記入。
	    private List<Review> top3;
	    
	    //写真と投稿時間を表示させるため必要。
	    @DateTimeFormat(iso = ISO.DATE)
	    private Date createdAt;
	    
	    private int evaluateCount;
	    
	    private String icon;

	}
