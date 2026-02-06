package com.example.demo.constants.errorTypes;

public enum CartErrorType {
    CART_EMPTY("CART_001"),
    CART_NOT_FOUND("CART_002"),
    PRODUCT_OUT_OF_STOCK("CART_003"),
    PRODUCT_NOT_IN_CART("CART_004"),
    QUANTITY_EXCEEDED("CART_005"),
    INVALID_CART_OPERATION("CART_006"),
    PRODUCT_NOT_FOUND("CART_007"),
    INVALID_PRODUCT_ID("CART_008"),
    INVALID_QUANTITY("CART_009"),
    CART_ITEM_NOT_FOUND("CART_010"),
    CART_DELETE_FAILED("CART_011");

    private final String code;

    CartErrorType(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
