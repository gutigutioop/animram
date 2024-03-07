package com.example.animram.form;

import java.util.List;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.NumberFormat;
import org.springframework.web.multipart.MultipartFile;

import com.example.animram.validation.constraints.ImageByte;
import com.example.animram.validation.constraints.ImageNotEmpty;

import lombok.Data;

@Data
public class ShopForm {
	
	private Long id;
	
	private Long userId;
	
	private String shopPath;
	
	@ImageNotEmpty
    @ImageByte(max = 2000000)
	private MultipartFile image;

	
    private String imageData;
    
    private Double latitude;

    private Double longitude;
	
    @NotEmpty
    @Size(max = 30)
	private String address;
	
    @NotEmpty
    @Size(max = 50)
	private String access;
	
    //@Pattern(regexp ="^[0-9A-Za-z]+$")
    @NotNull
    @Min(value=1)
    @NumberFormat(pattern = "#,###円")
	private Integer money;
	
	@NotEmpty
    @Size(max = 1000)
	private String shopDescription;
	
	private UserForm user;
	
	private List<ShopReviewForm> shopreviews;
	
	private double shopEvaluateCount;

	private double shopEvaluateCountDouble;
	
}
