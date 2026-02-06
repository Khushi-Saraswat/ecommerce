package com.example.demo.exception.Product;

import com.example.demo.constants.errorTypes.ProductErrorType;
import com.example.demo.exception.BaseException;

public class ProductException extends BaseException {
    public ProductException(String message, ProductErrorType type) {
        super(message, "PRODUCT_" + type.name());
    }
}
