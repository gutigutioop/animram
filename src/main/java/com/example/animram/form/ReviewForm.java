package com.example.animram.form;

import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import com.example.animram.entity.User;

import lombok.Data;

/*写真に対するレビューを保持するクラス
 *アニマルIDとユーザーIDで写真に対してのレビューを関連付ける。
 */
@Data
	public class ReviewForm {
	
		//レビューID
	    private Long id;
	    
	    //アニマルID
	    private Long animalId;
	    
	    //ユーザーID
	    private Long userId;
	    
	    private Integer evaluatereviewCount;
	    
	    //写真に対するレビュー。
	    @NotEmpty
	    @Size(max = 1000)
	    private String review;
	    
	   
	    //レビューに対していいねしたユーザーのリスト。
	    private List<User>  evaluateUsers;
	    
	  //今ログインしているユーザーがそのレビューに対していいねしたかのフラグ。したらtrue してなかったらfalse
	    private boolean  evaluatedByMe;
	    
	    private Long sort;
	    
	    private Integer type;
	    
	    
	    
	}
