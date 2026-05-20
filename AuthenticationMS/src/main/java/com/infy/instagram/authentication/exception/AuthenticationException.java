package com.infy.instagram.authentication.exception;

import org.springframework.http.HttpStatus;

public class AuthenticationException extends Exception {

	private static final long serialVersionUID = 1L;

	private final HttpStatus httpStatus;

	public AuthenticationException(String msg) {
		super(msg);
		this.httpStatus = HttpStatus.BAD_REQUEST;
	}

	public AuthenticationException(String msg, HttpStatus httpStatus) {
		super(msg);
		this.httpStatus = httpStatus;
	}

	public HttpStatus getHttpStatus() {
		return httpStatus;
	}

}
