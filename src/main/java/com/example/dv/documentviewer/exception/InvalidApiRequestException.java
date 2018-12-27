package com.example.dv.documentviewer.exception;

import org.springframework.http.HttpStatus;

public class InvalidApiRequestException extends RuntimeException {
	private static final long serialVersionUID = 12222L;

	private HttpStatus errorCode;

	public InvalidApiRequestException(String message) {
		super(message);
	}

	public InvalidApiRequestException(HttpStatus errorCode, String message) {
		super(message);
		this.errorCode = errorCode;
	}

	public HttpStatus getErrorCode() {
		return this.errorCode;
	}

}
