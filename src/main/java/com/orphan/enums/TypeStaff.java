package com.orphan.enums;

public enum TypeStaff {
    NhanVien("NhanVien"),
    CanBo("CanBo");

    private String code;

    TypeStaff(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
