package com.quant.columns.impl;

import com.quant.columns.Column;
import com.quant.validators.impl.BrandValidator;
import com.quant.validators.impl.PriceValidator;

public class PriceColumn implements Column<PriceValidator, Double> {

    private final PriceValidator validator = new PriceValidator();

    @Override
    public String getName() {
        return "Price";
    }

    @Override
    public PriceValidator getValidator() {
        return validator;
    }

    @Override
    public String getDisplayFormat() {
        return "%.2f Kč";
    }

}
