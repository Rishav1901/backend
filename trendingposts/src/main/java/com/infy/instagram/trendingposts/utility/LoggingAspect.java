package com.infy.instagram.trendingposts.utility;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class LoggingAspect {

    @Around("execution(* com.infy.instagram.trendingposts.*.*(..)) && !within(com.infy.instagram.trendingposts.config.*.*)")
    public Object logExecution(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        try {
            log.info("Entering {} with args={}", joinPoint.getSignature(), joinPoint.getArgs());
            Object result = joinPoint.proceed();
            log.info("Exiting {} in {} ms", joinPoint.getSignature(), System.currentTimeMillis() - start);
            return result;
        } catch (Throwable ex) {
            log.error("Exception in {} after {} ms: {}", joinPoint.getSignature(), System.currentTimeMillis() - start, ex.getMessage(), ex);
            throw ex;
        }
    }
}