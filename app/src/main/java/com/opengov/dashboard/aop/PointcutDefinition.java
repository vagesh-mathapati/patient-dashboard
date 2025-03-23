package com.opengov.dashboard.aop;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class PointcutDefinition {
    @Pointcut("within(com.opengov.dashboard..*)")
    public void adminServices() {
    }
}
