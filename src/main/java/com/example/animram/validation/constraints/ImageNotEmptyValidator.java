package com.example.animram.validation.constraints;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.web.multipart.MultipartFile;

public class ImageNotEmptyValidator implements ConstraintValidator<ImageNotEmpty, MultipartFile> {

    @Override
    public void initialize(ImageNotEmpty annotation) {
    }

    @Override
    public boolean isValid(MultipartFile image, ConstraintValidatorContext context) {
        if (image.isEmpty()) {
            return false;
        }
        return true;
    }
}
