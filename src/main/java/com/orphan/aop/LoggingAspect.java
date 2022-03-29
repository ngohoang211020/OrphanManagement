package com.orphan.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Method;

/**
 * Aspect for logging execution of api
 */
@Aspect
@Configuration
public class LoggingAspect {

    private final Logger log = LoggerFactory.getLogger(LoggingAspect.class);

    /**
     * Show log api
     *
     * @param pjp
     * @return
     * @throws Throwable
     */
    @Around("execution(* com.orphan.api.*Controller.*(..))")
    public Object profile(ProceedingJoinPoint pjp) throws Throwable {
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method method = signature.getMethod();
        String info = method.getName() + " - (" + pjp.getSignature().getDeclaringTypeName() + ")";
        long start = System.currentTimeMillis();
        log.info("###########API LOGGING############");
        log.info("Going to call the method:" + info);
        Object output = pjp.proceed(pjp.getArgs());
        log.info("Method execution completed:" + info);
        long elapsedTime = System.currentTimeMillis() - start;
        log.info("Method execution time: " + elapsedTime + " miliseconds.");
        log.info("###########END-API LOGGING#########");

        return output;
    }
}
