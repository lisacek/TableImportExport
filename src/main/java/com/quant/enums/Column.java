package com.quant.enums;

import com.quant.exceptions.ColumnValidationFailedException;
import com.quant.validators.Validator;
import com.quant.validators.impl.*;

public enum Column {
    PRODUCT_ID(ProductIdValidator.class),
    BRAND(BrandValidator.class),
    NAME(NameValidator.class),
    AMOUNT(AmountValidator.class),
    PRICE(PriceValidator.class);

    private final Class<? extends Validator> validatorClass;

    Column(Class<? extends Validator> validatorClass) {
        this.validatorClass = validatorClass;
    }

    public void validate(String value) throws ColumnValidationFailedException {
        Validator validator;
        try {
            validator = validatorClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        validator.validate(value);
    }

    public static Column getColumn(String head) {
        return Column.valueOf(head.toUpperCase().replace(" ", "_"));
    }
}
