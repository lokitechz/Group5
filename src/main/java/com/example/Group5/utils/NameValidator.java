package com.example.Group5.utils;

import com.example.Group5.entity.AppUser;
import com.example.Group5.repository.AppUserRepo;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

public class NameValidator implements ConstraintValidator<RepeatedName, String> {
    @Autowired
    AppUserRepo userRepo;

    @Override
    public void initialize(RepeatedName constraintAnnotation) {
    }

    @Override
    public boolean isValid(String value,ConstraintValidatorContext context) {
        List<AppUser> listUser = (List<AppUser>) userRepo.findAll();
        String input = value;
        for (AppUser appUser : listUser) {
            if(appUser.getUserName().equals(input)){
                return false;
            }
        }
        return true;
    }
}
