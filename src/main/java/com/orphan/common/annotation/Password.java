package com.orphan.common.annotation;

import com.orphan.validator.PasswordValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PasswordValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Password {
    String message() default "error.msg.password-invalid";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
