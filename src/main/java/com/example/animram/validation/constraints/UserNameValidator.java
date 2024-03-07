package com.example.animram.validation.constraints;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.animram.entity.User;
import com.example.animram.repository.UserRepository;

public class UserNameValidator implements ConstraintValidator<UserName, String> {

    @Autowired
    UserRepository userRepository;

    @Override
    public void initialize(UserName constraintAnnotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        
        User user = userRepository.findByName(value);
        if(user!=null) {
            return false;
        }
        
        return true;
    }
}