package com.example.animram.validation.constraints;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Documented
@Constraint(validatedBy = {UserNameValidator.class})
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface UserName{

    String message() default "このお名前は既に登録されています";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    
    @Target({ ElementType.FIELD })
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    public @interface List {
    	UserName[] value();
    }
}