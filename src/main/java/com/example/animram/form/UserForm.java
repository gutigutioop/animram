package com.example.animram.form;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import org.springframework.web.multipart.MultipartFile;

import com.example.animram.validation.constraints.PasswordEquals;
import com.example.animram.validation.constraints.UserName;

import lombok.Data;

@Data
@PasswordEquals
public class UserForm {
	
    @NotEmpty
    @Size(max = 20)
    @UserName
    private String name;

    @NotEmpty
    @Email
    private String email;

    @NotEmpty
    @Size(max = 20)
    //半角英数字のみ
    //@Pattern(regexp = "^[a-zA-Z0-9]+$")
    private String password;

    @NotEmpty
    @Size(max = 20)
    private String passwordConfirmation;
    
    @Size(max = 100)
    private String pet_name;
    
    private Long ageId;
    
    private MultipartFile ava;

    //private String imageData;
    
    @Size(max = 500)
    private String pr;
    
    
}
