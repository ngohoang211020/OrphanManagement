package com.orphan.exception;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

/**
 * Badrequest Exception
 */
@Getter
@Setter
public class BadRequestException extends Exception {

    public static final String ID_FOR_UPDATE_IS_INVALID = "ID_FOR_UPDATE_IS_INVALID";
    public static final String ERROR_INVALID_TOKEN = "ERROR_INVALID_TOKEN";
    public static final String EMAIL_IS_INVALID = "EMAIL_IS_INVALID";
    public static final String PHONE_NUMBER_IS_INVALID = "PHONE_NUMBER_IS_INVALID";
    public static final String PASSWORD_IS_INVALID = "PASSWORD_IS_INVALID";
    public static final String IDENTIFICATION_IS_INVALID = "IDENTIFICATION_IS_INVALID";
    public static final String ERROR_EMAIL_ALREADY_EXIST = "ERROR_EMAIL_ALREADY_EXIST";
    public static final String ERROR_IDENTIFICATION_ALREADY_EXIST = "ERROR_IDENTIFICATION_ALREADY_EXIST";

    public static final String ACCOUNT_NAME_IS_INVALID = "ACCOUNT_NAME_IS_INVALID";
    public static final String ERROR_REGISTER_USER_INVALID = "ERROR_REGISTER_USER_INVALID";
    public static final String ERROR_RESET_PASSWORD_BAD_REQUEST = "ERROR_RESET_PASSWORD_BAD_REQUEST";
    public static final String ERROR_CHANGE_PASSWORD_BAD_REQUEST = "ERROR_CHANGE_PASSWORD_BAD_REQUEST";
    private static final long serialVersionUID = 1L;
    public static final String ERROR_PASSWORD_NOT_MATCH_CONFIRM_PASSWORD = "ERROR_PASSWORD_NOT_MATCH_CONFIRM_PASSWORD";
    public static final String EMAIL_ALREADY_EXIST = "ERROR_EMAIL_ALREADY_EXIST";
    public static final String ERROR_SOCIAL_LOGIN_REQUEST = "ERROR_SOCIAL_LOGIN_REQUEST";
    public static final String ERROR_SAVE_FILE = "ERROR_SAVE_FILE";

    private String error;
    private String message;
    private String error_code;
    private boolean isJson;
    @JsonIgnore
    private boolean isPrintStackTrace;

    public BadRequestException() {
        super();
    }

    public BadRequestException(String message, boolean isJson) {
        super(message);
        this.message = message;
        this.isJson = isJson;
    }

    public BadRequestException(String error) {
        this.error = error;
    }

    public BadRequestException(String error_code, String message) {
        super(message);
        this.error = error_code;
        this.message = message;
        this.isJson = true;
    }

    public BadRequestException(String error, String message, boolean isJson) {
        super(message);
        this.error = error;
        this.message = message;
        this.isJson = isJson;
    }
}
