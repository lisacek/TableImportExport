package com.quant.columns.impl;

import com.quant.columns.Column;
import com.quant.validators.impl.NameValidator;
import com.quant.validators.impl.ProductIdValidator;

public class NameColumn implements Column<NameValidator, String> {

    private final NameValidator validator = new NameValidator();

    @Override
    public String getName() {
        return "Name";
    }

    @Override
    public NameValidator getValidator() {
        return validator;
    }
}
