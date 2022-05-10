package com.orphan.enums;

public enum RequestStatus {
    IN_PROGRESS("IN_PROGRESS"),
    DONE("DONE"),
    PENDING("PENDING"),
    DELETED("DELETED");

    private String code;

    RequestStatus(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
