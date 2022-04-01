package com.orphan.common.annotation;

import com.orphan.validator.IdentificationValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = IdentificationValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Identification {
    String message() default "{error.msg.identification-number-invalid}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
