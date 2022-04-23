package com.orphan.enums;

public enum ERole {
    ROLE_ADMIN("ROLE_ADMIN"),
    ROLE_MANAGER_HR("ROLE_MANAGER_HR"),
    ROLE_MANAGER_CHILDREN ("ROLE_MANAGER_CHILDREN"),
    ROLE_MANAGER_LOGISTIC ("ROLE_MANAGER_LOGISTIC"),
    ROLE_EMPLOYEE("ROLE_EMPLOYEE");
    private String code;

    ERole(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
