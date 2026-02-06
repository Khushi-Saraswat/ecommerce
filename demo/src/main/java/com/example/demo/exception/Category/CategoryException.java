package com.example.demo.exception.Category;

import com.example.demo.constants.errorTypes.CategoryError;
import com.example.demo.exception.BaseException;

public class CategoryException extends BaseException {

    public CategoryException(String message, CategoryError type) {
        super(message, "ORDER_" + type.name());
    }
}
