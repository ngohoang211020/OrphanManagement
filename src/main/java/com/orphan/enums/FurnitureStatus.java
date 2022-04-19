package com.orphan.enums;

public enum FurnitureStatus {
    GOOD("GOOD"),
    NEED_FIX("NEED_FIX"),
    NEED_REPLACE("NEED_REPLACE");

    private String code;

    FurnitureStatus(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
