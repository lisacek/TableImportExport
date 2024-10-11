package com.quant.validators.impl;

import com.quant.exceptions.ColumnValidationFailedException;
import com.quant.validators.Validator;

public class ProductIdValidator implements Validator {

    @Override
    public void validate(String value) throws ColumnValidationFailedException {
        if (value == null || value.isBlank()) {
            throw new ColumnValidationFailedException("Product Id cannot be empty");
        }

        try {
            Long.valueOf(value);
        } catch (NumberFormatException e) {
            throw new ColumnValidationFailedException("Product Id is not a valid number");
        }
    }

}
