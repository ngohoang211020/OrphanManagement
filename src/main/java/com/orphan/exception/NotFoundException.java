package com.orphan.exception;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

/**
 * NotFound Exception
 *
 */
@Getter
@Setter
public class NotFoundException extends Exception {

    public static final String ERROR_USER_NOT_FOUND = "ERROR_USER_NOT_FOUND";

    public static final String ERROR_CHILDREN_NOT_FOUND = "ERROR_CHILDREN_NOT_FOUND";

    public static final String ERROR_NOTIFICATION_NOT_FOUND = "ERROR_NOTIFICATION_NOT_FOUND";

    public static final String ERROR_ORPHAN_INTRODUCER_NOT_FOUND = "ERROR_ORPHAN_INTRODUCER_NOT_FOUND";

    public static final String ERROR_ORPHAN_NURTURER_NOT_FOUND = "ERROR_ORPHAN_NURTURER_NOT_FOUND";

    public static final String ERROR_FURNITURE_NOT_FOUND = "ERROR_FURNITURE_NOT_FOUND";

    public static final String ERROR_STAFF_NOT_FOUND = "ERROR_STAFF_NOT_FOUND";
  

    public static final String ERROR_EVENT_NOT_FOUND = "ERROR_EVENT_NOT_FOUND";

    public static final String ERROR_FURNITURE_REQUEST_FORM_NOT_FOUND = "ERROR_FURNITURE_REQUEST_FORM_NOT_FOUND";
    public static final String ERROR_FURNITURE_CATEGORY_NOT_FOUND = "ERROR_FURNITURE_CATEGORY_NOT_FOUND";
    public static final String ERROR_PICNIC_NOT_FOUND="ERROR_PICNIC_NOT_FOUND";

    private static final long serialVersionUID = 1L;
    private String error;
    private String message;
    private HttpStatus httpStatus;
    @JsonIgnore
    private boolean isPrintStackTrace;

    public NotFoundException() {
        super();
    }

    public NotFoundException(String error, String message) {
        super(message);
        this.error = error;
        this.message = message;
    }

    public NotFoundException(String error, String message, HttpStatus httpStatus) {
        super(message);
        this.error = error;
        this.message = message;
        this.httpStatus = httpStatus;
    }
}
