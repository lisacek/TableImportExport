package com.quant.validators.impl;

import com.quant.exceptions.ColumnValidationFailedException;
import com.quant.validators.Validator;

public class AmountValidator implements Validator {

    @Override
    public void validate(String value) throws ColumnValidationFailedException {
        if (value == null || value.isBlank()) {
            throw new ColumnValidationFailedException("Amount cannot be empty");
        }

        long amount;
        try {
            amount = Long.parseLong(value);
        } catch (NumberFormatException e) {
            throw new ColumnValidationFailedException("Amount is not a valid number");
        }

        if (amount < 0) {
            throw new ColumnValidationFailedException("Amount cannot be negative");
        }
    }

}
