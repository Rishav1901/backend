package com.infy.instagram.follow.exception;

import org.springframework.http.HttpStatus;


//Exception
public class FollowException extends Exception {

   private static final long serialVersionUID = 1L;

   private final HttpStatus httpStatus;

   public FollowException(String msg) {
       super(msg);
       this.httpStatus = HttpStatus.BAD_REQUEST;
   }

   public FollowException(String msg, HttpStatus httpStatus) {
       super(msg);
       this.httpStatus = httpStatus;
   }

   public HttpStatus getHttpStatus() {
       return httpStatus;
   }
}
