package com.orphan.validator;

import com.orphan.common.annotation.Identification;
import com.orphan.utils.constants.Constants;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class IdentificationValidator implements ConstraintValidator<Identification, String> {

    @Override
    public boolean isValid(String identification, ConstraintValidatorContext context) {
        return identification.matches(Constants.IDENTIFICATION_PATTERN);
    }
}
