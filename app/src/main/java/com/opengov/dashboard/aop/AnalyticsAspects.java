package com.opengov.dashboard.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class AnalyticsAspects {
    @Before(value = "com.opengov.dashboard.common.aop.PointcutDefinition.adminServices()")
    public void logBefore(JoinPoint joinPoint) {
        log.trace("Entering Method: {} " ,joinPoint.getSignature());
    }

    @After(value = "com.opengov.dashboard.common.aop.PointcutDefinition.adminServices()")
    public void logAfter(JoinPoint joinPoint) {
        log.trace("Exiting Method: {}" , joinPoint.getSignature());
    }
}
