package com.infy.instagram.follow.util;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

import jakarta.validation.ConstraintViolationException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.infy.instagram.follow.exception.FollowException;
//globalExceptiions
@RestControllerAdvice
public class GlobalExceptionHandler {

   @ExceptionHandler(MethodArgumentNotValidException.class)
   public ResponseEntity<ErrorMessage> handleValidationExceptions(
           MethodArgumentNotValidException ex) {

       ErrorMessage error = new ErrorMessage();

       error.setErrCode(HttpStatus.BAD_REQUEST.value());

       error.setErrMessage(
               ex.getBindingResult()
                 .getAllErrors()
                 .stream()
                 .map(DefaultMessageSourceResolvable::getDefaultMessage)
                 .collect(Collectors.joining(", "))
       );

       error.setTimeStamp(LocalDateTime.now().toString());

       return new ResponseEntity<>(
               error,
               HttpStatus.BAD_REQUEST
       );
   }

   @ExceptionHandler(ConstraintViolationException.class)
   public ResponseEntity<ErrorMessage> handleConstraintValidationExceptions(
           ConstraintViolationException ex) {

       ErrorMessage error = new ErrorMessage();

       error.setErrCode(HttpStatus.BAD_REQUEST.value());

       error.setErrMessage(
               ex.getConstraintViolations()
                 .stream()
                 .map(violation -> violation.getMessage())
                 .collect(Collectors.joining(", "))
       );

       error.setTimeStamp(LocalDateTime.now().toString());

       return new ResponseEntity<>(
               error,
               HttpStatus.BAD_REQUEST
       );
   }

   @ExceptionHandler(IllegalArgumentException.class)
   public ResponseEntity<ErrorMessage> handleIllegalArgumentException(
           IllegalArgumentException exception) {

       ErrorMessage error = new ErrorMessage();

       error.setErrCode(HttpStatus.BAD_REQUEST.value());

       error.setErrMessage(exception.getMessage());

       error.setTimeStamp(LocalDateTime.now().toString());

       return new ResponseEntity<>(
               error,
               HttpStatus.BAD_REQUEST
       );
   }

   @ExceptionHandler(FollowException.class)
   public ResponseEntity<ErrorMessage> handleFollowException(
           FollowException exception) {

       ErrorMessage error = new ErrorMessage();

       error.setErrCode(exception.getHttpStatus().value());

       error.setErrMessage(exception.getMessage());

       error.setTimeStamp(LocalDateTime.now().toString());

       return new ResponseEntity<>(
               error,
               exception.getHttpStatus()
       );
   }

   @ExceptionHandler(Exception.class)
   public ResponseEntity<ErrorMessage> handleExceptions(
           Exception exception) {

       ErrorMessage error = new ErrorMessage();

       error.setErrCode(HttpStatus.INTERNAL_SERVER_ERROR.value());

       error.setErrMessage(
               "An internal server error occurred. Please contact support."
       );

       error.setTimeStamp(LocalDateTime.now().toString());

       return new ResponseEntity<>(
               error,
               HttpStatus.INTERNAL_SERVER_ERROR
       );
   }
}
