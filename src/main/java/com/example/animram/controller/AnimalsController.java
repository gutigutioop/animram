package com.example.animram.controller;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FilenameUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.animram.constants.AnimalType;
import com.example.animram.entity.Animal;
import com.example.animram.entity.Evaluate;
import com.example.animram.entity.Review;
import com.example.animram.entity.Session;
import com.example.animram.entity.User;
import com.example.animram.entity.UserInf;
import com.example.animram.form.AnimalForm;
import com.example.animram.form.EvaluateForm;
import com.example.animram.form.ReviewForm;
import com.example.animram.form.UserForm;
import com.example.animram.repository.AnimalRepository;
import com.example.animram.repository.EvaluateRepository;
import com.example.animram.repository.ReviewRepository;

	@Controller
	public class AnimalsController {
	
		@Autowired
		private ModelMapper modelMapper;

		@Autowired
		private AnimalRepository repository;
		
		@Autowired
		private ReviewRepository reviewRepository;
		
		@Autowired
		private EvaluateRepository evaluateRepository;

		@Autowired
		private HttpServletRequest request;
		
		@Autowired
		private  Session session;

		@Value("${image.local:false}")
		private String imageLocal;

		// ランキング表示時の王冠アイコンの配列。
		private static String[] icons = {"/images/icons/oukan.png", "/images/icons/oukan2.png", "/images/icons/oukan3.png"};
		
       //①笑顔の写真新規投稿画面表示用メソッド。3回同じ@GetMappingメソッド記入する。 enum使用にて新規投稿画面表示時に<input type="hidden" th:field="*{type}" />に番号が最初に割り当てられる。
		@GetMapping("/animals/new_smile")
		public String newAnimal_smile(Model model) {
			AnimalForm animal = new AnimalForm();
			//AnimalType animaltype = new AnimalType();　列挙型はstatic型なのでインスタンス必要なし。クラス名.フィールド名で表示できる。
			//AnimalType animaltype = AnimalType.Smile;
			animal.setType(AnimalType.Smile.getN());
			model.addAttribute("form", animal);
			return "animals/new_smile";
		}
		
		//②怒り顔の写真新規投稿画面表示用メソッド。
		@GetMapping("/animals/new_angry")
		public String newAnimal_angry(Model model) {
			AnimalForm animal = new AnimalForm();
			animal.setType(AnimalType.Angry.getN());
			/*写真新規投稿画面表示に"hidden"タグで最初に０を表示。新規投稿時のみDBに０を保存。
			animal.setEvaluateCount(0);
			animal.setReviewCount(0);*/
			model.addAttribute("form", animal);
			return "animals/new_angry";
		}
		
		//③悲顔の写真新規投稿画面表示用メソッド。
		@GetMapping("/animals/new_sad")
		public String newAnimal_sad(Model model) {
			AnimalForm animal = new AnimalForm();
			animal.setType(AnimalType.Sad.getN());
			model.addAttribute("form", animal);
			return "animals/new_sad";
		}
		
		
		//ランキング画面表示メソッド。
		@GetMapping("/rankings")
		 public String ranking(Principal principal, Model model) throws IOException {
		        Authentication authentication = (Authentication) principal;
		        UserInf user = (UserInf) authentication.getPrincipal();
		        
		       
		        
		        //最初にAnimalFormのlistを作成。
		        List<AnimalForm> list = new ArrayList<>();
		        //エンティティanimals(list)の中にいいね数上位３つの写真の情報をrepositoryを利用して詰める。
		        List<Animal> animals = repository.findTop3ByOrderByEvaluateCountDesc();
		        //上位３つの写真の情報をfor文で一つずつanimalエンティティに詰める。
		        int i = 0;
		        for(Animal animal : animals) {
		        	//animal.getId();※書かなくてよい。
		        	//animalエンティティの写真情報を一つずつユーザーが見やすいようにviewに表示するためにformに一個ずつ編集して詰めかえる。
		        	AnimalForm form = getAnimal(user, animal);
		        	form.setIcon(icons[i]);
		        	/*上位いいね数写真1枚ずつレビュー数多い順3つのレビューをanimalIdで検索・取り出しreviewsの中に入れる。写真3枚分のレビュー情報全てを1個ずつform.setTop3(reviews)に詰める必要あるから
		        	 * for文の中にList<Review> reviews = reviewRepository.findTop3ByAnimalIdOrderByEvaluatereviewCountDesc(animal.getId());を記入しても大丈夫。
		        	 * 最初、findTop3ByAnimalIdOrderByEvaluatereviewCountDesc(animals.get(0));とlistのgetメソッドでanimalIdを取り出そうとしたらエラーなった。
		        	 * animalエンティティのlist型のanimalsで情報getしてもidのみを取り出せないので単のanimalの中からgetIdする。*/
		        	List<Review> reviews = reviewRepository.findTop3ByAnimalIdOrderByEvaluatereviewCountDesc(animal.getId());
		        	form.setTop3(reviews);
		        	//listの中に全ての情報を詰める。
		        	list.add(form);
		        	i++;
		        }
		         
		        
		        //写真情報をlistに詰める。
		        //List<AnimalForm> list = new ArrayList<>();
		        //for (Animal entity : animals) {
		        //}
		        

	       
		        model.addAttribute("list", list);
		     
		        
		       return "animals/ranking";
			}
		
		//ReviewFormの中にreviewEntityのレビュー・レビュー数を詰めるメソッド。
		//public ReviewForm getReview(Review reviewEntity){
		//ReviewForm form2 = new ReviewForm();
		//form2.setReview(reviewEntity.getReview());
		//form2.setEvaluatereviewCount(reviewEntity.getEvaluatereviewCount());
		//return form2;
		//}
		
		
		//①写真新規投稿フォーム入力後メソッド。allcreateメソッドにそれぞれ飛んでcreateで保存処理する。
				@PostMapping("/animals/new_smile")
				public String smile_create(Principal principal, @Validated @ModelAttribute("form") AnimalForm form, BindingResult result,
						Model model, @RequestParam MultipartFile image, RedirectAttributes redirAttrs)
								throws IOException {
					 if (result.hasErrors()) {
				            model.addAttribute("hasMessage", true);
				            model.addAttribute("class", "alert-danger");
				            model.addAttribute("message", "投稿に失敗しました。");
				            
				            return "animals/new_smile";
					 }

				            allcreate(form,principal, image);
				            
				            redirAttrs.addFlashAttribute("hasMessage", true);
				            redirAttrs.addFlashAttribute("class", "alert-info");
				            redirAttrs.addFlashAttribute("message", "投稿に成功しました。");
				            
				            //Long sor =   session.getSort();
		                    //return "animals/new_smile";
				            return "redirect:/animals/smile_index?sort=1";
				           
				}
				//②写真新規投稿フォーム入力後メソッド。allcreateメソッドにそれぞれ飛んでcreateで保存処理する。
				@PostMapping("/animals/new_angry")
				public String angry_create(Principal principal, @Validated @ModelAttribute("form") AnimalForm form, BindingResult result,
						Model model, @RequestParam MultipartFile image, RedirectAttributes redirAttrs)
								throws IOException {
					 if (result.hasErrors()) {
				            model.addAttribute("hasMessage", true);
				            model.addAttribute("class", "alert-danger");
				            model.addAttribute("message", "投稿に失敗しました。");
				            
				            return "animals/new_angry";
					 }

				           allcreate(form,principal, image);
				            
				            redirAttrs.addFlashAttribute("hasMessage", true);
				            redirAttrs.addFlashAttribute("class", "alert-info");
				            redirAttrs.addFlashAttribute("message", "投稿に成功しました。");
				            
				            return "redirect:/animals/angry_index?sort=1";
				           
				}
				//③写真新規投稿フォーム入力後メソッド。allcreateメソッドにそれぞれ飛んでcreateで保存処理する。
				@PostMapping("/animals/new_sad")
				public String sad_create(Principal principal, @Validated @ModelAttribute("form") AnimalForm form, BindingResult result,
						Model model, @RequestParam MultipartFile image, RedirectAttributes redirAttrs)
								throws IOException {
					 if (result.hasErrors()) {
				            model.addAttribute("hasMessage", true);
				            model.addAttribute("class", "alert-danger");
				            model.addAttribute("message", "投稿に失敗しました。");
				            
				            return "animals/new_sad";
					 }

				            allcreate(form,principal, image);
				            
				            redirAttrs.addFlashAttribute("hasMessage", true);
				            redirAttrs.addFlashAttribute("class", "alert-info");
				            redirAttrs.addFlashAttribute("message", "投稿に成功しました。");
				            
		                    
				            return "redirect:/animals/sad_index?sort=1";
				           
				}
		
			//投稿保存処理本体メソッド
			private void allcreate(AnimalForm form, Principal principal,
					MultipartFile image)
			//保存処理書いて、一覧ページへ飛んでいく処理書く。引き数はform validation model multipart
					throws IOException{
	//	Boolean isImageLocal= Boolean.valueOf(false);
				boolean isImageLocal = false;
		        if (imageLocal != null) {
		            isImageLocal = new Boolean(imageLocal);
		        }
		        Animal entity = new Animal();
		        //
		        Authentication authentication = (Authentication) principal;
		        UserInf user = (UserInf) authentication.getPrincipal();
		        entity.setUserId(user.getUserId());
		        File destFile = null;
		        if (isImageLocal) {
		            destFile = saveImageLocal(image, entity);
		            entity.setPath(destFile.getAbsolutePath());
		        } else {
		            entity.setPath("");
		        }
		        entity.setDescription(form.getDescription());
		        entity.setType(form.getType());
		        entity.setEvaluateCount(0);
		        entity.setReviewCount(0);
		        repository.saveAndFlush(entity);
			}
			//画像ローカル保存。
			private File saveImageLocal(MultipartFile image, Animal entity) 
				throws IOException {
				String uploadsDir = "/uploads/";
				String realPathToUploads = request.getServletContext().getRealPath(uploadsDir);
		    
	
		        if (!new File(realPathToUploads).exists()) {
		            new File(realPathToUploads).mkdir();
		        }
		        String fileName = image.getOriginalFilename();
		        Date c = new Date();
		        File destFile = new File(realPathToUploads, fileName.format("%tH時 %tM分 %tS秒", c, c, c));
		        image.transferTo(destFile);
	
		        return destFile;
		    }
		
		//①笑顔一覧ページ遷移メソッド。if分岐で1.新着順2.投稿順3.人気順4.レビュー数順に遷移する。
		   @GetMapping("animals/smile_index")  //animals/smile_index?URL    sort=1~パラメーター
		    public String smileIndex(Principal principal, @RequestParam("sort") long sort,  Model model) throws IOException {
		        Authentication authentication = (Authentication) principal;
		        UserInf user = (UserInf) authentication.getPrincipal();
		        
		     List<Animal> animals = null;
		     
				if(sort==1){
					animals = repository.findByTypeOrderByUpdatedAtDesc(AnimalType.Smile.getN());
				}
				else if(sort==2){
					animals = repository.findByTypeOrderByUpdatedAtAsc(AnimalType.Smile.getN());
				}
				else if(sort==3){
					animals = repository.findByTypeOrderByEvaluateCountDesc(AnimalType.Smile.getN());
				}
				
				else if(sort==4) {
					animals = repository.findByTypeOrderByReviewCountDesc(AnimalType.Smile.getN());
				}
				
				 List<AnimalForm> list = new ArrayList<>();
		        for (Animal entity : animals) {
		        	AnimalForm form = getAnimal(user, entity);
		            list.add(form);
		        }
		        
		       //session.setSort(sort);
		       //model.addAttribute(session);
		        model.addAttribute("list", list);
		        model.addAttribute("sort",sort);
		        return "animals/smile_index";
		    }
		   
		 
		
			
										
		
		//②怒顔一覧ページ遷移メソッド。
		  @GetMapping("animals/angry_index")
	    public String angryIndex(Principal principal,@RequestParam("sort") long sort, Model model) throws IOException {
	        Authentication authentication = (Authentication) principal;
	        UserInf user = (UserInf) authentication.getPrincipal();
	        
	        List<Animal> animals = null;
	        
	        
		     
			if(sort==1){
				animals = repository.findByTypeOrderByUpdatedAtDesc(AnimalType.Angry.getN());
			}
			else if(sort==2){
				animals = repository.findByTypeOrderByUpdatedAtAsc(AnimalType.Angry.getN());
			}
			else if(sort==3){
				animals = repository.findByTypeOrderByEvaluateCountDesc(AnimalType.Angry.getN());
			}
			
			else if(sort==4) {
				animals = repository.findByTypeOrderByReviewCountDesc(AnimalType.Angry.getN());
			}

	       // List<Animal> animals = repository.findByTypeOrderByUpdatedAtDesc(AnimalType.Angry.getN());
			
	        List<AnimalForm> list = new ArrayList<>();
	        for (Animal entity : animals) {
	        	AnimalForm form = getAnimal(user, entity);
	            list.add(form);
	        }
	        
	        model.addAttribute("list", list);
	        model.addAttribute("sort",sort);

	        return "animals/angry_index";
	    }
		 
		
		//③悲顔一覧ページ遷移メソッド。
		  @GetMapping("animals/sad_index")
	    public String sadIndex(Principal principal, @RequestParam("sort") long sort, Model model) throws IOException {
	        Authentication authentication = (Authentication) principal;
	        UserInf user = (UserInf) authentication.getPrincipal();
	        
	        List<Animal> animals = null;
		     
			if(sort==1){
				animals = repository.findByTypeOrderByUpdatedAtDesc(AnimalType.Sad.getN());
			}
			else if(sort==2){
				animals = repository.findByTypeOrderByUpdatedAtAsc(AnimalType.Sad.getN());
			}
			else if(sort==3){
				animals = repository.findByTypeOrderByEvaluateCountDesc(AnimalType.Sad.getN());
			}
			
			else if(sort==4) {
				animals = repository.findByTypeOrderByReviewCountDesc(AnimalType.Sad.getN());
			}
	        
	        //List<Animal> animals = repository.findByTypeOrderByUpdatedAtDesc(AnimalType.Sad.getN());
	        List<AnimalForm> list = new ArrayList<>();
	        for (Animal entity : animals) {
	        	AnimalForm form = getAnimal(user, entity);
	            list.add(form);
	        }
	        model.addAttribute("list", list);
	        model.addAttribute("sort",sort);
	        
	        return "animals/sad_index";
	    }
		  
		
		 
		//全一覧内容表示用メソッド。
		 public AnimalForm getAnimal(UserInf user, Animal entity) throws FileNotFoundException, IOException {
		        modelMapper.getConfiguration().setAmbiguityIgnored(true);
		        modelMapper.typeMap(Animal.class, AnimalForm.class).addMappings(mapper -> mapper.skip(AnimalForm::setUser));
		        modelMapper.typeMap(Animal.class, AnimalForm.class).addMappings(mapper -> mapper.skip(AnimalForm::setEvaluates));
		        modelMapper.typeMap(Animal.class, AnimalForm.class).addMappings(mapper -> mapper.skip(AnimalForm::setReviews));
		        

		        boolean isImageLocal = false;
		        if (imageLocal != null) {
		            isImageLocal = new Boolean(imageLocal);
		        }
		        AnimalForm form = modelMapper.map(entity, AnimalForm.class);
		        
		        if (isImageLocal) {
		            try (InputStream is = new FileInputStream(new File(entity.getPath()));
		                    ByteArrayOutputStream os = new ByteArrayOutputStream()) {
		                byte[] indata = new byte[10240 * 16];
		                int size;
		                while ((size = is.read(indata, 0, indata.length)) > 0) {
		                    os.write(indata, 0, size);
		                }
		                StringBuilder data = new StringBuilder();
		                data.append("data:");
		                data.append(getMimeType(entity.getPath()));
		                data.append(";base64,");

		                //data.append(new String(Base64Utils.encode(os.toByteArray())));
		               //data.append(new String(Base64Utils.encode(os.toByteArray()), "ASCII"));
		                data.append(Base64.getEncoder().encodeToString(os.toByteArray()));
		               //data.append(new String( Base64.getEncoder().encodeToString(os.toByteArray())));
		                form.setImageData(data.toString());
		            }
		            
		            
		        }
               //ユーザー情報一覧表示。
		        UserForm userForm = modelMapper.map(entity.getUser(), UserForm.class);
		        form.setUser(userForm);

		        //写真に対するいいね一覧表示
		         List<EvaluateForm> evaluates = new ArrayList<EvaluateForm>();
				for (Evaluate evaluateEntity : entity.getEvaluates()) {
					EvaluateForm evaluate = modelMapper.map(evaluateEntity, EvaluateForm.class);
					evaluates.add(evaluate);
					//自分がいいねした写真ならばフラグを立てる。
					if (user.getUserId().equals(evaluateEntity.getUserId())) {
						
						form.setEvaluatedByMe(true);
					}
				}
				form.setEvaluates(evaluates);
				
				
				//写真に対するレビュー・レビューのいいね一覧表示。
				//レビューのlistを作成。いいねの総数を数えたり自分がいいねしたか確認したりレビューを表示する為に必要。
				List<ReviewForm> Reviews = new ArrayList<ReviewForm>();
				//form.setReviewEvaluatedByMe(false);
				//アニマル(写真に対する今までのレビュー)をレビューentityに詰めてから、レビューentityとレビューformをマッピングさせる。
				for (Review reviewEntity : entity.getReviews()) {
					ReviewForm review = modelMapper.map(reviewEntity, ReviewForm.class);
					//list<ReviewForm>Reviewsにaddする前に、レビューのformにいいねしたかのフラグを設定する。(自分がいいねしたかの)
					//レビューにいいねした人たちの情報を一つずつevaluateReviewEntityに詰める。
					for(User evaluateReviewEntity : review.getEvaluateUsers()) {
						//詰めた情報の中から一つずつログインユーザーIDといいねしたユーザーIDが一致してるのを抜き出す。
						if (user.getUserId().equals(evaluateReviewEntity.getUserId())) {
							//ログインユーザーIDといいねしたユーザーIDが一致したらレビューに詰める。誤ってanimalformにつめたら写真に対するいいねフラグとダブルので注意。
							review.setEvaluatedByMe(true);
						}
					}
					//Reviewsに今までのレビュー一覧、レビューいいねを詰める。
					Reviews.add(review);
				}
				//レビューいいね数を詰める。
				form.setReviews(Reviews);
				
			return form;
		 }

		    private String getMimeType(String path) {
		        String extension = FilenameUtils.getExtension(path);
		        String mimeType = "image/";
		        switch (extension) {
		        case "jpg":
		        case "jpeg":
		            mimeType += "jpeg";
		            break;
		        case "png":
		            mimeType += "png";
		            break;
		        case "gif":
		            mimeType += "gif";
		            break;
		        }
		        return mimeType;
		    }
		
}

