package com.orphan.validator;

import com.orphan.common.annotation.Date;
import com.orphan.utils.constants.Constants;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class DateValidator implements ConstraintValidator<Date, String> {
    @Override
    public boolean isValid(String date, ConstraintValidatorContext context) {
        if (date == "" || date.isEmpty()) return false;
        return date.matches(Constants.DATE_PATTERN);

    }
}
