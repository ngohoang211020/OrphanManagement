package com.orphan.common.annotation;

import com.orphan.validator.PhoneValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PhoneValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
//cho biết việc bạn chỉ có thể sử dụng annotation với field va method.
@Retention(RetentionPolicy.RUNTIME)
// cho biết annotation có thể được truy cập qua reflection tại thời điểm runtime, nếu bạn không
// khai báo nó khi định nghĩa một annotation bạn sẽ không thể truy cập nó tại thời điểm runtime.
public @interface Phone {
    String message() default "{error.msg.phone-number-invalid}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
