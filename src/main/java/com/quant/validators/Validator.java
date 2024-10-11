package com.quant.validators;

import com.quant.exceptions.ColumnValidationFailedException;

public interface Validator {

    public void validate(String value) throws ColumnValidationFailedException;

}
