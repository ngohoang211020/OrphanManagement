package com.orphan.enums;

public enum ERole {
    ROLE_ADMIN("ROLE_ADMIN"),
    ROLE_MANAGER("ROLE_MANAGER");

    private String code;

    ERole(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
