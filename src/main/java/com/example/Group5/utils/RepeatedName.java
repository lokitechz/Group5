package com.example.Group5.utils;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = NameValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RepeatedName {
    String message() default "Tên tài khoản đã bị trùng";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
