package com.example.animram.controller;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FilenameUtils;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.animram.entity.Shop;
import com.example.animram.entity.ShopReview;
import com.example.animram.entity.UserInf;
import com.example.animram.form.ShopForm;
import com.example.animram.form.ShopReviewForm;
import com.example.animram.repository.ShopRepository;
import com.example.animram.repository.ShopReviewRepository;
import com.example.animram.service.ShopService;

@Controller
public class ShopsController {
	
	protected static Logger log = LoggerFactory.getLogger(ShopsController.class);
	
	@Autowired
    private ModelMapper modelMapper;
	
	@Autowired
	private ShopRepository repository;
	
	@Autowired
	private ShopReviewRepository reviewRepository;
	
	@Autowired
	private ShopService shopService;
	
	@Autowired
	private HttpServletRequest request;
	
	@Value("${image.local:false}")
	private String imageLocal;

	//@Value("${spring.data.web.pageable.one-indexed-parameters:false}")
	//private int  Local;
	
	//ショップ投稿画面に遷移するメソッド。
	@GetMapping("/new_shop")
 	public String newShop(Model model) {
		ShopForm form = new ShopForm();
		model.addAttribute("form",form);
	 return "shops/new_shop";
	}
	
	//入力したショップ情報DB保存メソッド。
	@PostMapping("/shops/new_shop")
	public String create(Principal principal,@Validated @ModelAttribute("form") ShopForm form, BindingResult result, @RequestParam MultipartFile image, Model model, RedirectAttributes redirAttrs)
			throws IOException {
		Authentication authentication = (Authentication) principal;
        UserInf user = (UserInf) authentication.getPrincipal();
        
        if (result.hasErrors()) {
        	model.addAttribute("hasMessage", true);
			model.addAttribute("class", "alert-danger");
			model.addAttribute("message", "登録に失敗しました。");
			//model.addAttribute("form", form);
			
            return "shops/new_shop";
        }
        
		Shop entity = modelMapper.map(form,Shop.class);
		entity.setUserId(user.getUserId());
		File destFile = null;
		String path = "";
		if(!image.isEmpty()) {
		destFile = saveImageLocal(image,entity);
		}
		path = destFile.getAbsolutePath();
		entity.setShopPath(path);
		repository.saveAndFlush(entity);
		
		return "redirect:/shop";
	}
		
	//ショップの写真を保存している場所を指定する。場所を指定終わったら68行目に戻る
	   private File saveImageLocal(MultipartFile image, Shop entity) throws IOException {
		   String uploadsDir = "/uploads/";
		  
		   String realPathToUploads = request.getServletContext().getRealPath(uploadsDir);
			//existメソッドでファイル(フルパス)の存在を確かめ、存在がなければ新しいファイルを作成する
			if (!new File(realPathToUploads).exists()) {
				//fileがなかったら作成する。
				new File(realPathToUploads).mkdir();
			}
			//投稿アバターの名前を取得。
			String fileName = image.getOriginalFilename();
			//親抽象パス名と子パス名文字列。もし親がnullならば、子パスでFileインスタンスが生成される。C:/uploads(親パス)/○○(子パス)
			File destFile = new File(realPathToUploads, fileName);
			//C:/uploads(親パス)/○○(子パス)で画像を移動「する。
			//path名とファイル名設定したファイルオブジェクトを作成。アップロードした画像を作成したファイルオブジェクトに移動する。
			image.transferTo(destFile);
			
			//setGeoInfo(entity, destFile, image.getOriginalFilename());

			return destFile;
	}
	   
	  /* private void setGeoInfo(Shop entity, BufferedInputStream inputStream, String fileName)
				throws ImageProcessingException, IOException, ImageReadException {
			Metadata metadata = ImageMetadataReader.readMetadata(inputStream);
			setGeoInfo(entity, metadata, inputStream, null, fileName);
		}

		private void setGeoInfo(Shop entity, File destFile, String fileName)
				throws ImageProcessingException, IOException, ImageReadException {
			Metadata metadata = ImageMetadataReader.readMetadata(destFile);
			setGeoInfo(entity, metadata, null, destFile, fileName);
		}

		private void setGeoInfo(Shop entity, Metadata metadata, BufferedInputStream inputStream, File destFile,
				String fileName) {
			if (log.isDebugEnabled()) {
				for (Directory directory : metadata.getDirectories()) {
					for (Tag tag : directory.getTags()) {
						log.debug("{} {}", tag.toString(), tag.getTagType());
					}
				}
			}

			try {
				IImageMetadata iMetadata = null;
				if (inputStream != null) {
					iMetadata = Sanselan.getMetadata(inputStream, fileName);
					IOUtils.closeQuietly(inputStream);
				}
				if (destFile != null) {
					iMetadata = Sanselan.getMetadata(destFile);
				}
				if (iMetadata != null) {
					GPSInfo gpsInfo = null;
					if (iMetadata instanceof JpegImageMetadata) {
						gpsInfo = ((JpegImageMetadata) iMetadata).getExif().getGPS();
						if (gpsInfo != null) {
							log.debug("latitude={}", gpsInfo.getLatitudeAsDegreesNorth());
							log.debug("longitude={}", gpsInfo.getLongitudeAsDegreesEast());
							entity.setLatitude(gpsInfo.getLatitudeAsDegreesNorth());
							entity.setLongitude(gpsInfo.getLongitudeAsDegreesEast());
						}
					} else {
						List<?> items = iMetadata.getItems();
						for (Object item : items) {
							log.debug(item.toString());
						}
					}
				}
			} catch (ImageReadException | IOException e) {
				log.warn(e.getMessage(), e);
			}
		}*/
	   
	   //ショップのレビュー・評価投稿画面遷移用メソッド。
	   @GetMapping("/shops/{shopId}/reviews/new")
	   public String reviewEvaluateNew(@PathVariable("shopId") long shopId,Model model) {
		   ShopReviewForm form = new ShopReviewForm();
		   form.setShopId(shopId);
		   model.addAttribute("form", form);
		   return "shops/shop_review";
	   }
	   
	   //ショップのレビュー・評価DB保存メソッド。
	   @PostMapping("/shops/{shopId}/review")
	   public String reviewEvaluate(Principal principal, @PathVariable("shopId") long shopId,@Validated @ModelAttribute("form") ShopReviewForm form ,BindingResult result, Model model) {
		   Authentication authentication = (Authentication) principal;
	       UserInf user = (UserInf) authentication.getPrincipal();
	       List<ShopReview> submit = reviewRepository.findByUserIdAndShopId(user.getUserId(),shopId);
	       
	       if(submit.size()==1) {
	    	   model.addAttribute("hasMessage", true);
				model.addAttribute("class", "alert-danger");
				model.addAttribute("message", "既にレビュー・評価されてます。");
				
	            return "redirect:/shop";
	       }
	       
	       if (form.getShopReview().equals("")&&form.getShopEvaluate().equals(0)) {
	        	model.addAttribute("hasMessage", true);
				model.addAttribute("class", "alert-danger");
				model.addAttribute("message", "どちらかを入力して登録して下さい。");
				
	            return "redirect:/shop";
	        }
	       
	       ShopReview entity = new ShopReview();
	      
	       entity.setUserId(user.getUserId());
	       entity.setShopReview(form.getShopReview());
	       entity.setShopEvaluate(form.getShopEvaluate());
	       entity.setShopId(shopId);
	       reviewRepository.saveAndFlush(entity);
	       
	       model.addAttribute("hasMessage", true);
		   model.addAttribute("class", "alert-danger");
		   model.addAttribute("message", "レビュー・評価投稿しました。");
			
	       
		   return "redirect:/shop";
	   }
	   
	   
	   //投稿したショップ一覧表示メソッド。
	   @GetMapping("/shop")
	 	public String shopIndex(Principal principal,  @PageableDefault(page = 0, size = 3,direction = Direction.DESC, sort = {"id"}) Pageable pageable,Model model) throws NullPointerException, FileNotFoundException, IOException{
		   Authentication authentication = (Authentication) principal;
	        UserInf user = (UserInf) authentication.getPrincipal();
	        
	        Page<Shop> shopPages = shopService.getShops(pageable);//何ページ分表示したいかdata初期値持ってくる。何ページ目に遷移したいって情報入ってない。
	       List<Shop> shops   = shopPages.getContent();
	        List<ShopForm> list = new ArrayList<>();
	        
	        //shopidを取り出して、shopentityの情報を評価順に取り出す。
	        //List<ShopReview> uu = reviewRepository. findAllBy();
	        //Long yy = uu.getShopId();
	        //List<Shop>  shops = repository.findByAll(yy);
	        
	       for(Shop entity : shops ) {
	    	  
	    	   Double evaluateCount = reviewRepository.selectEvaluatesAvg(entity.getId());
	    	   if(evaluateCount ==null) {
	    		   evaluateCount = 0.0;
	    	   }	   
	    	   
	    	   double evaluateCountDouble = ((double)Math.round(evaluateCount * 10))/10;
	    	   
	    	   ShopForm form = getShop(entity,user);
	    	   form.setShopEvaluateCountDouble(evaluateCountDouble);
	    	   list.add(form);
	    	   
	       }
	       model.addAttribute("pagenation",shopPages);
	       model.addAttribute("list",list);
	       model.addAttribute("sankou", 1);
		 	return "shops/shop_index";
	 }
	   
	   //住所絞り込み検索後一覧表示。
	   @GetMapping("/shops")
	   public String narrowDown(Principal principal, @RequestParam("address") String address, @PageableDefault(page = 0, size = 3,direction = Direction.DESC, sort = {"id"}) Pageable pageable, Model model)throws FileNotFoundException, IOException {
		   Authentication authentication = (Authentication) principal;
		   UserInf user = (UserInf) authentication.getPrincipal();
		   
		   List<ShopForm> list = new ArrayList<>();
		   //List <Shop> shops = new ArrayList<>();
		   Page<Shop> shopPages = shopService.getShopNarrowDown("%" + address + "%",pageable);
		   List <Shop> shops = shopPages.getContent();
		   
		   
		   for(Shop entity : shops ) {
			   ShopForm form = getShop(entity,user);
			   list.add(form);
		   }
		   model.addAttribute("pagenation",shopPages);
		   model.addAttribute("list",list);
		   model.addAttribute("sankou",2);
		   model.addAttribute("address",address);
		   return "shops/shop_index";
	   }
	   
	   
	   //一覧表示する内容を整えて、formにセットして108行目に戻る。
	   public ShopForm getShop(Shop entity,UserInf user)throws FileNotFoundException, IOException {
		   modelMapper.getConfiguration().setAmbiguityIgnored(true);
	       // modelMapper.typeMap(Shop.class, ShopForm.class).addMappings(mapper -> mapper.skip(ShopForm::setUser));
		
		   
		   boolean isImageLocal = false;
	        if (imageLocal != null) {
	            isImageLocal = new Boolean(imageLocal);
	        }
	        
	        ShopForm form = modelMapper.map(entity, ShopForm.class);
	        
	        if(isImageLocal) {
	        	
	        	String path = entity.getShopPath();
							        
	        	try(FileInputStream fis = new FileInputStream(path);BufferedInputStream is = new BufferedInputStream(fis);){
	        		 ByteArrayOutputStream os = new ByteArrayOutputStream();
	        		 byte[] indata = new byte[10240*16];
		                int size;
		                while ((size = is.read(indata, 0, indata.length)) > 0) {
		                    os.write(indata, 0, size);
		                }
	        		
	        		/*int line;
	        		while (true) {
	        		    line = is.read();
	        		    if (line == -1) {
	        		        break;
	        		    }
	        		   
	        		}*/
		                String mimetype = getMimeType(entity.getShopPath());
		              
		                String base = new String(Base64.getEncoder().encodeToString(os.toByteArray()));
		                String data = "data:" + mimetype + ";base64," + base;
		                form.setImageData(data);
	        	}catch (Exception e) {
	        		e.printStackTrace();
	        	} catch (OutOfMemoryError e) {
	        		e.printStackTrace();
	        	}
	        	
	        }
	        
	        List<ShopReviewForm> shopreviews = new ArrayList<>();
	        for(ShopReview shopreview : entity.getShopReviews()) {
	        	ShopReviewForm shopreviewform = modelMapper.map(shopreview, ShopReviewForm.class);
	        	 shopreviews.add(shopreviewform);
	        }
	        
	        form.setShopreviews(shopreviews);
	        
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
	   

