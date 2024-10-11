package com.quant.enums;

public enum Head {
    PRODUCT_ID,
    BRAND,
    NAME,
    AMOUNT,
    PRICE;

    public static Head getHead(String head) {
        return Head.valueOf(head.toUpperCase().replace(" ", "_"));
    }
}
