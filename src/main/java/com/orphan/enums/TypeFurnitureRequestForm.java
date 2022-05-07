package com.orphan.enums;

public enum TypeFurnitureRequestForm {
    REPAIR("REPAIR"),
    IMPORT("IMPORT");

    private String code;

    TypeFurnitureRequestForm(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
