package com.infy.instagram.authentication.util;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.infy.instagram.authentication.exception.AuthenticationException;
import com.infy.instagram.authentication.exception.UserException;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;

@RestControllerAdvice
public class ExceptionControllerAdvice {

	private final Environment environment;

	public ExceptionControllerAdvice(Environment environment) {
		this.environment = environment;
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorMessage> handleValidationExceptions(MethodArgumentNotValidException ex) {

		ErrorMessage error = new ErrorMessage();
		error.setErrCode(HttpStatus.BAD_REQUEST.value());
		error.setErrMessage(
				ex.getBindingResult().getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage)
						.collect(Collectors.joining(", ")));
		error.setTimeStamp(LocalDateTime.now().toString());
		return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<ErrorMessage> handleConstraintValidationExceptions(ConstraintViolationException ex) {

		ErrorMessage error = new ErrorMessage();
		error.setErrCode(HttpStatus.BAD_REQUEST.value());
		error.setErrMessage(ex.getConstraintViolations().stream().map(ConstraintViolation::getMessage)
				.collect(Collectors.joining(", ")));
		error.setTimeStamp(LocalDateTime.now().toString());
		return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	}

	
	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<ErrorMessage> handleIllegalArgumentException(IllegalArgumentException exception) {

		ErrorMessage error = new ErrorMessage();
		error.setErrCode(HttpStatus.BAD_REQUEST.value());
		error.setErrMessage(exception.getMessage());
		error.setTimeStamp(LocalDateTime.now().toString());
		return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	}

	
	@ExceptionHandler(AuthenticationException.class)
	public ResponseEntity<ErrorMessage> handleExceptions(AuthenticationException exception) {

		ErrorMessage error = new ErrorMessage();
		HttpStatus status = exception.getHttpStatus();
		error.setErrCode(status.value());
		error.setErrMessage(environment.getProperty(exception.getMessage(), exception.getMessage()));
		error.setTimeStamp(LocalDateTime.now().toString());
		return new ResponseEntity<>(error, status);
	}

	@ExceptionHandler(UserException.class)
	public ResponseEntity<ErrorMessage> handleUserExceptions(UserException exception) {

		ErrorMessage error = new ErrorMessage();
		HttpStatus status = exception.getHttpStatus();
		error.setErrCode(status.value());
		error.setErrMessage(environment.getProperty(exception.getMessage(), exception.getMessage()));
		error.setTimeStamp(LocalDateTime.now().toString());
		return new ResponseEntity<>(error, status);
	}

	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<ErrorMessage> handleRuntimeExceptions(RuntimeException exception) {

		ErrorMessage error = new ErrorMessage();
		error.setErrCode(HttpStatus.BAD_REQUEST.value());
		error.setErrMessage(exception.getMessage());
		error.setTimeStamp(LocalDateTime.now().toString());
		return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	}

	// Feature 18: professional generic error message without leaking internals
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorMessage> handleExceptions(Exception exception) {

		ErrorMessage error = new ErrorMessage();
		error.setErrCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
		error.setErrMessage("An internal server error occurred: " + exception.getMessage());
		error.setTimeStamp(LocalDateTime.now().toString());
		return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
