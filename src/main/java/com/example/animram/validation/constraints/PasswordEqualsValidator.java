package com.example.animram.validation.constraints;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.util.ObjectUtils;

public class PasswordEqualsValidator implements ConstraintValidator<PasswordEquals, Object> {

    private String field1;
    private String field2;
    private String message;

    @Override
    public void initialize(PasswordEquals annotation) {
        field1 = "password";
        field2 = "passwordConfirmation";
        message = annotation.message();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        BeanWrapper beanWrapper = new BeanWrapperImpl(value);
        Object field1Value =  beanWrapper.getPropertyValue(field1);
        Object field2Value = beanWrapper.getPropertyValue(field2);
     
        boolean matched = ObjectUtils.nullSafeEquals(field1Value, field2Value);

        if (matched) {
            return true;
        
        } else {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(message).addPropertyNode(field2).addConstraintViolation();
            return false;
        }
    }

	

}

