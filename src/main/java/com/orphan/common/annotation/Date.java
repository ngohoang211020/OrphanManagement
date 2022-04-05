package com.orphan.common.annotation;

import com.orphan.validator.DateValidator;
import com.orphan.validator.IdentificationValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = DateValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Date {
    String message() default "{error.msg.date-invalid}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
