package com.orphan.enums;

public enum UserStatus {
    ACTIVED("ACTIVED"),
    DELETED("DELETED");

    private String code;

    UserStatus(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
