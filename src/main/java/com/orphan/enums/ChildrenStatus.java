package com.orphan.enums;

public enum ChildrenStatus {
    RECEIVED("Đã được nhận nuôi"),
    WAIT_TO_RECEIVE("Đang ở trung tâm");

    private String code;

    ChildrenStatus(String code) {
        this.code = code;
    }
}
