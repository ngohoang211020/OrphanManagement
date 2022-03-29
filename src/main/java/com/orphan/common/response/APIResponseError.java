package com.orphan.common.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * A value object representing a api error response data
 *
 */
@Getter
@Setter
@Builder
public class APIResponseError implements Serializable {
	private static final long serialVersionUID = 1L;
	
	public long timestamp;
    public int line;
    public int code;
    public String error;
    public Object message;

    public APIResponseError() {
    }

    public APIResponseError(int line, int code, Object message, String error) {
        this.timestamp = new Date().getTime();
        this.line = line;
        this.code = code;
        this.message = message;
        this.error = error;
    }

    public APIResponseError(long timestamp, int line, int code, String error, Object message) {
        this.timestamp = new Date().getTime();
        this.line = line;
        this.code = code;
        this.error = error;
        this.message = message;
    }
}
