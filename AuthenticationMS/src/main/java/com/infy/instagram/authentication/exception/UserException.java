package com.infy.instagram.authentication.exception;

import org.springframework.http.HttpStatus;

public class UserException extends Exception {
	private static final long serialVersionUID = 2L;

	private final HttpStatus httpStatus;

	public UserException(String msg) {
		super(msg);
		this.httpStatus = HttpStatus.BAD_REQUEST;
	}

	public UserException(String msg, HttpStatus httpStatus) {
		super(msg);
		this.httpStatus = httpStatus;
	}

	public HttpStatus getHttpStatus() {
		return httpStatus;
	}

}
