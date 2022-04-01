package com.orphan.validator;

import com.orphan.common.annotation.Phone;
import com.orphan.utils.constants.Constants;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PhoneValidator implements ConstraintValidator<Phone, String> {

    @Override
    public boolean isValid(String phone, ConstraintValidatorContext context) {
        return phone.matches(Constants.PHONE_PATTERN);
    }
}
