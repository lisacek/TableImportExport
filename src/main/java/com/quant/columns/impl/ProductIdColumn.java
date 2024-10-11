package com.quant.columns.impl;

import com.quant.columns.Column;
import com.quant.validators.impl.ProductIdValidator;

public class ProductIdColumn implements Column<ProductIdValidator, Long> {

    private final ProductIdValidator validator = new ProductIdValidator();

    @Override
    public String getName() {
        return "Product ID";
    }

    @Override
    public ProductIdValidator getValidator() {
        return validator;
    }

    @Override
    public boolean isEditable() {
        return false;
    }
}
