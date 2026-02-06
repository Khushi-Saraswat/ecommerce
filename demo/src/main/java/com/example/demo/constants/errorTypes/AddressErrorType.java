package com.example.demo.constants.errorTypes;

public enum AddressErrorType {
    ADDRESS_NOT_FOUND("ADDRESS_001"),
    INVALID_ADDRESS_ID("ADDRESS_002"),
    USER_NOT_AUTHORIZED("ADDRESS_003"),
    DEFAULT_ADDRESS_NOT_SET("ADDRESS_004"),
    ADDRESS_NOT_SAVED("ADDRESS_005"),
    ADDRESS_NOT_UPDATED("ADDRESS_006"),
    NO_ADDRESSES_FOUND("ADDRESS_007");

    private final String code;

    AddressErrorType(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
