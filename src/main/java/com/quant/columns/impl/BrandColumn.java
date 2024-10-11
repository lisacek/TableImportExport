package com.quant.columns.impl;

import com.quant.columns.Column;
import com.quant.validators.impl.BrandValidator;

public class BrandColumn implements Column<BrandValidator, String> {

    private final BrandValidator validator = new BrandValidator();

    @Override
    public String getName() {
        return "Brand";
    }

    @Override
    public BrandValidator getValidator() {
        return validator;
    }
}
