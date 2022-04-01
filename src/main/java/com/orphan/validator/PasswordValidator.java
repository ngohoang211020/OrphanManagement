package com.orphan.validator;

import com.orphan.common.annotation.Password;
import com.orphan.utils.constants.Constants;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordValidator implements ConstraintValidator<Password, String> {


    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        return password.matches(Constants.IDENTIFICATION_PATTERN);
    }
}
