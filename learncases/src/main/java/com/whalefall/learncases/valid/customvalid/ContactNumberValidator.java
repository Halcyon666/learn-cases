package com.whalefall.learncases.valid.customvalid;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;


/**
 * @author WhaleFall
 * @date 2022-06-04 22:29
 */
@Slf4j
public class ContactNumberValidator implements
        ConstraintValidator<ContactNumberConstraint, String> {

    @Override
    public boolean isValid(String contactField,
                           ConstraintValidatorContext cxt) {
        // 电话为9-13位数字
        return contactField != null && contactField.matches("\\d+")
                && (contactField.length() > 8) && (contactField.length() < 14);
    }

}
