package com.example.animram.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Data;

/*写真に対するレビューをデータベースのreviewsテーブルと対応付けるクラス
 * アニマルIDとユーザーIDで写真に対してレビュー投稿。
 */

	@Entity
	@Table(name = "reviews")
	@Data
	public class Review extends AbstractEntity implements Serializable {
	    private static final long serialVersionUID = 1L;

	    //プライマリーキーのレビューID。自動連番機能付。
	    @Id
	    @SequenceGenerator(name = "review_id_seq")
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;

	    //アニマルID。
	    @Column(nullable = false)
	    private Long animalId;
	    
	    //ユーザーID。
	    @Column(nullable = false)
	    private Long userId;
	    
	    
	    //写真に対するレビュー。
	    @Column(nullable = false, length = 1000)
	    private String review;
	    
	    //レビューに対するいいね数カウントの為作成。
	    @Column(nullable = false)
	    private Integer evaluatereviewCount;
	    
	    /*
	    @ManyToOne
	    @JoinColumn(name = "animalId", insertable = false, updatable = false)
	    private Animal animal;
	    */
	    
	    //EvaluateReviewを中間テーブルに設定し、Animalテーブルと多対多の関係つける。レビューとユーザーも多対多。evaluateUsersはレビューに対していいねしたユーザーのリスト。
	    @ManyToMany
	     @JoinTable(name = "evaluate_reviews",
	    		 joinColumns = @ JoinColumn(name = "reviewId"),
	    		 inverseJoinColumns = @JoinColumn(name =  "userId" )
	     )
	    private List<User> evaluateUsers;
	    
	    //今ログインしているユーザーがそのレビューに対していいねしたかのフラグ。したらtrue してなかったらfalse。review entityにはいらない。
	    //private boolean  evaluatedByMe;
	   
	}
