package com.insta.post.utility;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import com.insta.post.exception.InstaPostManagementException;


@Aspect
@Component
public class LoggingAspect {

	private static final Log LOGGER = LogFactory.getLog(LoggingAspect.class);

	@AfterThrowing(pointcut = "execution(* com.insta.post_management.service.*Impl.java.*(..))", throwing = "exception")
	public void logServiceException(InstaPostManagementException exception) {
		LOGGER.error(exception.getMessage(), exception);
	}

}
