package com.orphan.enums;

public enum ChildrenStatus {
    RECEIVED("RECEIVED"),
    WAIT_TO_RECEIVE("WAIT_TO_RECEIVE");

    private String code;

    ChildrenStatus(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
