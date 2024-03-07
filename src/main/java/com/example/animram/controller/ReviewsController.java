package com.example.animram.controller;

import java.security.Principal;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.animram.entity.Animal;
import com.example.animram.entity.Review;
import com.example.animram.entity.UserInf;
import com.example.animram.form.ReviewForm;
import com.example.animram.repository.AnimalRepository;
import com.example.animram.repository.ReviewRepository;

@Controller
public class ReviewsController {
	
	//入力されたレビューformの情報をentityに移し返し、リポジトリ―でDBにレビューを保存するため必要。
	@Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ReviewRepository repository;
    
    @Autowired
	private AnimalRepository animalRepository;

    //各投稿した写真に対するレビュー投稿画面遷移用メソッド。
    @GetMapping("/animals/{animalId}/reviews/new")
    public String newreview(@PathVariable("animalId") long animalId, @RequestParam("sort") long sort, @RequestParam("type") int type,  Model model) {
        ReviewForm form = new ReviewForm();
        form.setAnimalId(animalId);
        form.setEvaluatereviewCount(0);
        form.setType(type);
        form.setSort(sort);
        model.addAttribute("form", form);
        model.addAttribute("sort", sort);
        model.addAttribute("type", type);
        
        return "reviews/new";
    }
    
    //レビュー投稿用メソッド。
    @PostMapping("/animals/{animalId}/review/new")
    public String create(Principal principal,@PathVariable("animalId") long animalId,  @RequestParam("sort") long sort, @RequestParam("type") int type, @Validated @ModelAttribute("form") ReviewForm form,
            BindingResult result, Model model, RedirectAttributes redirAttrs) {
    	 	Authentication authentication = (Authentication) principal;
	        UserInf user = (UserInf) authentication.getPrincipal();
	        
	        	//レビュー投稿失敗時のメッセージ。
        		if (result.hasErrors()) {
        			model.addAttribute("hasMessage", true);
        			model.addAttribute("class", "alert-danger");
        			model.addAttribute("レビューが保存出来ませんでした。");
        			return "reviews/new";
        		}
        		
        		
        	//DBにレビュー保存※レビュー数カウントはレビューをDBに保存してからするのでコードの書く順番に注意する。
    		Review entity = modelMapper.map(form, Review.class);
    		 //mapperを使わない場合。Review entity = new Review();　 entity.setAnimalId(form.getAnimalId());　 entity.setUserId(form.UserId());　entity.setReview(form.getReview());と書いてもOK。
    		 //entity.setReview(form.getReview());を書いてないのはmodelMapperで既にentityの中に入ってるから。
    		 //例：setAnimalId等の	setは名前・値を置き換える為のもの。値を取ってくる時に使わない。取ってくるのはget。
    		 //実験:entity.setAnimalId(animalId);はmapperする時entityにsetされてるから書かなくてもレビュー投稿できた。逆にentity.setUserId(user.getUserId());はnot null設定してたからエラーになった。try catch必要？
    		entity.setAnimalId(animalId);
    		entity.setUserId(user.getUserId());
    		 //saveとflash同時にする。スプリングブート書き込みとDBセーブ一緒にする。しないとトランザクション失敗例と同じ事がおこり、処理実行が失敗する。
    		repository.saveAndFlush(entity);
    		
    		
    		//レビュー数カウント。animalRepository.findById(animalId)でanimalEntityにanimalidで指定された1枚の写真の情報を詰める。例:一つのanimalid番号4の中にreviewsテーブルの情報が４つ入ってる。
    		Animal animalEntity = animalRepository.findById(animalId).get();
    		 //一つの写真に今まで投稿した複数のレビューと今投稿したレビューを含めてlistのメソッドsizeで要素数をint型に代入する。sizeの戻り値はint型。isempty contains は boolean型。
            int u = animalEntity.getReviews().size();
            animalEntity.setReviewCount(u);
             //saveとflash同時にする。スプリングブート書き込みとDBセーブ一緒にする。しないとトランザクション失敗例と同じ事がおこり、処理実行が失敗する。
            animalRepository.saveAndFlush(animalEntity);
            
            //レビュー投稿成功時のメッセージ。
    		redirAttrs.addFlashAttribute("hasMessage", true);
    		redirAttrs.addFlashAttribute("class", "alert-info");
    		redirAttrs.addFlashAttribute("message","レビュー投稿出来ました。");
    		
         
           type = form.getType();
           sort = form.getSort();
    		
    		if(type==1){
    			return "redirect:/animals/smile_index?sort=" + sort;
			}
			else if(type==2){
				return "redirect:/animals/angry_index?sort=" + sort;
			}
			else {
				return "redirect:/animals/sad_index?sort=" + sort;
			}
    
    }
}
