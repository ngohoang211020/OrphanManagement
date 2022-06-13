package com.orphan.enums;

public enum FundType {
    FURNITURE_REQUEST("FURNITURE_REQUEST"),
    CHARITY_EVENT("CHARITY_EVENT"),
    PICNIC("PICNIC");

    private String code;

    FundType(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
