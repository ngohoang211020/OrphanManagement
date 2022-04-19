package com.orphan.enums;

public enum ERole {
    ROLE_ADMIN("admin"),
    ROLE_MANAGER("manager");

    private String code;

    ERole(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
