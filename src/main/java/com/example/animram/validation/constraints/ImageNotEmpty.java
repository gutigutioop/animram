package com.example.animram.validation.constraints;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.ReportAsSingleViolation;

@Documented
@Constraint(validatedBy = ImageNotEmptyValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@ReportAsSingleViolation
public @interface ImageNotEmpty {

    String message() default "写真を選択してください。";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}