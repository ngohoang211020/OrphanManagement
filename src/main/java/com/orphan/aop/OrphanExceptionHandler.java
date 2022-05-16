package com.orphan.aop;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.orphan.common.response.APIResponseError;
import com.orphan.exception.BadRequestException;
import com.orphan.exception.NotFoundException;
import com.orphan.utils.constants.APIConstants;
import com.orphan.utils.constants.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.io.IOException;

/**
 * OrphanExceptionHandler ExceptionHandler
 */
@ControllerAdvice
public class OrphanExceptionHandler {

    private final Logger log = LoggerFactory.getLogger(LoggingAspect.class);

    /**
     * Handle {@link AuthenticationException}
     *
     * @param e {@link AuthenticationException}
     * @returm {@link ResponseEntity} of {@link com.orphan.common.response.APIResponse}
     */
    @ExceptionHandler(value = {AuthenticationException.class})
    protected ResponseEntity<APIResponseError> handleAuthenticationException(com.orphan.exception.AuthenticationException e) {
        if (e.isPrintStackTrace()) {
            log.error(e.getMessage(), e);
        } else {
            log.error(e.getError() + " - [Message]: " + e.getMessage());
        }
        APIResponseError apiResponseError = APIResponseError.builder()
                .code(HttpStatus.UNAUTHORIZED.value())
                .error(e.getError())
                .message(e.getMessage())
                .build();
        log.warn(Constants.LOG_LEVEL_WARN + ":" + e.getMessage());
        return new ResponseEntity<>(apiResponseError, HttpStatus.UNAUTHORIZED);
    }

    /**
     * Handle {@link NotFoundException}
     *
     * @param e {@link NotFoundException}
     * @returm {@link ResponseEntity}
     */
    @ExceptionHandler(value = {NotFoundException.class})
    protected ResponseEntity<APIResponseError> handleNotFoundException(NotFoundException e) {
        if (e.isPrintStackTrace()) {
            log.error(e.getMessage(), e);
        } else {
            log.error(e.getError() + " - [Message]: " + e.getMessage());
        }
        APIResponseError apiResponseError = APIResponseError.builder()
                .code(HttpStatus.NOT_FOUND.value())
                .error(e.getError())
                .message(!StringUtils.isEmpty(e.getMessage()) ? e.getMessage() : APIConstants.ERROR_UNKNOWN_MSG)
                .build();
        return new ResponseEntity<>(apiResponseError, HttpStatus.NOT_FOUND);
    }

    /**
     * Handle {@link MethodArgumentTypeMismatchException}
     *
     * @param e {@link MethodArgumentTypeMismatchException}
     * @throws BadRequestException
     * @returm {@link ResponseEntity} of {@link com.orphan.common.response.APIResponse}
     */
    @ExceptionHandler(value = {MethodArgumentTypeMismatchException.class})
    protected ResponseEntity<APIResponseError> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        log.error(e.getMessage(), e);
        APIResponseError apiResponseError = APIResponseError.builder()
                .code(HttpStatus.BAD_REQUEST.value())
                .error(HttpStatus.BAD_REQUEST.name())
                .message(e.getMessage())
                .build();

        return new ResponseEntity<APIResponseError>(apiResponseError, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handle {@link BadRequestException}
     *
     * @param e {@link BadRequestException}
     * @throws IOException
     * @throws JsonMappingException
     * @throws JsonParseException
     * @returm {@link ResponseEntity} of {@link com.orphan.common.response.APIResponse}
     */
    @ExceptionHandler(value = BadRequestException.class)
    protected ResponseEntity<APIResponseError> handleBadRequestException(BadRequestException e) {
        if (e.isPrintStackTrace()) {
            log.error(e.getMessage(), e);
        } else {
            log.error(e.getError() + " - [Message]: " + e.getMessage());
        }
        APIResponseError apiResponseError = APIResponseError.builder()
                .code(HttpStatus.BAD_REQUEST.value())
                .error(e.getError())
                .message(!StringUtils.isEmpty(e.getMessage()) ? e.getMessage() : APIConstants.ERROR_UNKNOWN_MSG)
                .build();
        return new ResponseEntity<APIResponseError>(apiResponseError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<APIResponseError> handleAccessDeniedException(AccessDeniedException e) {
        log.error(e.getMessage(), e);
        APIResponseError apiResponseError = APIResponseError.builder()
                .code(HttpStatus.UNAUTHORIZED.value())
                .error(HttpStatus.UNAUTHORIZED.name())
                .message(!StringUtils.isEmpty(e.getMessage()) ? e.getMessage() : APIConstants.ERROR_UNKNOWN_MSG)
                .build();
        return new ResponseEntity<APIResponseError>(apiResponseError, HttpStatus.UNAUTHORIZED);
    }
}
