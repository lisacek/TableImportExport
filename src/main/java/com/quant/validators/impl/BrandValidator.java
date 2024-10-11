package com.quant.validators.impl;

import com.quant.exceptions.ColumnValidationFailedException;
import com.quant.validators.Validator;

public class BrandValidator implements Validator {

    @Override
    public void validate(String value) throws ColumnValidationFailedException {
        if (value == null || value.isBlank()) {
            throw new ColumnValidationFailedException("Brand cannot be empty");
        }
    }

}
