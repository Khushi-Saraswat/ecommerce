package com.example.demo.exception.Review;

import com.example.demo.constants.errorTypes.ReviewErrorType;
import com.example.demo.exception.BaseException;

public class ReviewException extends BaseException {

    private final ReviewErrorType type;

    public ReviewException(String message, ReviewErrorType type) {
        super(message, "REVIEW_" + type.name());
        this.type = type;
    }

    public ReviewErrorType getType() {
        return type;
    }
}
