package com.infy.instagram.follow.util;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import lombok.extern.log4j.Log4j2;

@Component
@Aspect
@Log4j2
public class LoggingAspect {

   @AfterThrowing(
       pointcut = "execution(* com.infy.instagram.follow.service.*Impl.*(..))",
       throwing = "exception"
   )
   public void logExceptionFromService(Exception exception) {

       log.error(
           exception.getMessage(),
           exception
       );
   }
}
