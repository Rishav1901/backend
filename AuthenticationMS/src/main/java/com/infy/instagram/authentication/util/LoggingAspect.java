package com.infy.instagram.authentication.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import com.infy.instagram.authentication.exception.AuthenticationException;

import lombok.extern.log4j.Log4j2;

@Component
@Aspect
@Log4j2
public class LoggingAspect {
	private static final Log LOGGER = LogFactory.getLog(LoggingAspect.class);
	
	@AfterThrowing(pointcut = "execution(* com.infy.service.*Impl.*(..))", throwing = "exception")
	public void logExceptionFromService(AuthenticationException exception) {
		LOGGER.error(exception.getMessage(), exception);
	}

}
