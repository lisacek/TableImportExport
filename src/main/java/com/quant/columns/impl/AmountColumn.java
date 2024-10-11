package com.quant.columns.impl;

import com.quant.columns.Column;
import com.quant.validators.impl.AmountValidator;
import com.quant.validators.impl.BrandValidator;

public class AmountColumn implements Column<AmountValidator, Long> {

    private final AmountValidator validator = new AmountValidator();

    @Override
    public String getName() {
        return "Amount";
    }

    @Override
    public AmountValidator getValidator() {
        return validator;
    }
}
