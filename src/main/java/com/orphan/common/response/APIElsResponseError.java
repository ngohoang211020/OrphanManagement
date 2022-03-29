package com.orphan.common.response;

import java.io.Serializable;

public class APIElsResponseError  implements Serializable {
    private static final long serialVersionUID = 1L;

    public String error_code;
    public String message;

    public APIElsResponseError() {
    }

    public APIElsResponseError(String error, String message) {
        this.message = message;
        this.error_code = error;
    }
}
