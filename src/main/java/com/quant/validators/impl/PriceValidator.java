package com.quant.validators.impl;

import com.quant.exceptions.ColumnValidationFailedException;
import com.quant.validators.Validator;

public class PriceValidator implements Validator {

    @Override
    public void validate(String value) throws ColumnValidationFailedException {
        if (value == null || value.isBlank()) {
            throw new ColumnValidationFailedException("Price cannot be empty");
        }

        double price;
        try {
            price = Double.parseDouble(value);
        } catch (NumberFormatException e) {
            throw new ColumnValidationFailedException("Price is not a valid number");
        }

        if (price < 0) {
            throw new ColumnValidationFailedException("Price cannot be negative");
        }

        if (Double.toString(price).split("\\.")[1].length() > 2 ) {
            throw new ColumnValidationFailedException("Price cannot have more than 2 decimal places");
        }
    }

}
