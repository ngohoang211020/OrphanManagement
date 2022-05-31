package com.orphan.enums;

public enum MailTrackingType {
    MAIL_TEMPLATE("MAIL_TEMPLATE"),
    MAIL_FEEDBACK("MAIL_FEEDBACK"),
    MAIL_NOTIFY("MAIL_NOTIFY");

    private String code;

    MailTrackingType(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
