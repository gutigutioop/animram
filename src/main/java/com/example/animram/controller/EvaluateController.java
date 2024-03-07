package com.example.animram.controller;

import java.security.Principal;
import java.util.List;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.animram.entity.Animal;
import com.example.animram.entity.Evaluate;
import com.example.animram.entity.Session;
import com.example.animram.entity.UserInf;
import com.example.animram.repository.AnimalRepository;
import com.example.animram.repository.EvaluateRepository;

@Controller
public class EvaluateController{
	@Autowired
	private ModelMapper modelMapper;
	@Autowired
	private EvaluateRepository repository;
	@Autowired
	private AnimalRepository animalRepository;
	@Autowired
	private Session session;
	
	//いいね登録メソッド。
	@PostMapping("/evaluate")	public String create(Principal principal,@RequestParam("animal_id") long animalId, @RequestParam("sort") long sort,
			@RequestParam("animal_type") int type, Model model, RedirectAttributes redirAttrs) {
		Authentication authentication = (Authentication) principal;
        UserInf user = (UserInf) authentication.getPrincipal();
        Long userId = user.getUserId();
        
        List<Evaluate> results = repository.findByUserIdAndAnimalId(userId, animalId);
        if (results.size() == 0) {
        	Evaluate entity = new Evaluate();
        	//EvaluateForm evaluate = modelMapper.map(entity, EvaluateForm.class);
        	//entity.setType(type); 
        	
            //いいね登録に必要なuserIdとanimalIdをDBに保存する。
            entity.setUserId(userId);
            entity.setAnimalId(animalId);
            repository.saveAndFlush(entity);
           
            //写真に対していいね登録した後、一つの写真に対するいいね数をカウントする。
            updateEvaluateCount(animalId);
            
            redirAttrs.addFlashAttribute("hasMessage", true);
            redirAttrs.addFlashAttribute("class", "alert-info");
            redirAttrs.addFlashAttribute("message","お気に入りに登録しました。");
            //redirAttrs.addFlashAttribute("sort",sort);
            
          // Long sor = session.getSort();
           //String sort2 = String.valueOf(sor);
            
        }
       // Long sor = session.getSort();
        //いいねした後、遷移先にリダイレクト。
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
	
	//いいね解除メソッド。
	 @DeleteMapping("/evaluate")
	    @Transactional
	    public String destroy(Principal principal, @RequestParam("animal_id") long animalId,@RequestParam("animal_type") int type, 
	    		@RequestParam("sort") long sort, RedirectAttributes redirAttrs) {
	        Authentication authentication = (Authentication) principal;
	        UserInf user = (UserInf) authentication.getPrincipal();
	        Long userId = user.getUserId();
	        List<Evaluate> results = repository.findByUserIdAndAnimalId(userId, animalId);
	        if (results.size() == 1) {
	            repository.deleteByUserIdAndAnimalId(user.getUserId(), animalId);
	            repository.flush();
	            
	          //写真に対していいね解除した後、一つの写真に対するいいね数をカウントする。
	            updateEvaluateCount(animalId);

	            redirAttrs.addFlashAttribute("hasMessage", true);
	            redirAttrs.addFlashAttribute("class", "alert-info");
	            redirAttrs.addFlashAttribute("message","お気に入りを解除しました。");
	        }
	        //Long sor = session.getSort();
	        //いいね解除した後、遷移先にリダイレクト。
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
	 
	 private void updateEvaluateCount(long animalId) {
         /*いいねが登録/削除されるとき、いいね登録メソッドの引き数で受け取ったanimalIdでAnimalエンティティのEvaluateCountを取得する。
          * EvaluateCountを+１ずつすることでいいね登録されるたびにいいね数を増やしていく。
          */
         Animal animalEntity = animalRepository.findById(animalId).get();
         int u = animalEntity.getEvaluates().size();
         animalEntity.setEvaluateCount(u);
         animalRepository.saveAndFlush(animalEntity);
	 }
}
