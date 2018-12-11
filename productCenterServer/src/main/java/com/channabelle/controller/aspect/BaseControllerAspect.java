package com.channabelle.controller.aspect;


import com.channabelle.common.utils.JsonDateValueProcessor;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Aspect
public class BaseControllerAspect {
    private Logger log = Logger.getLogger(BaseControllerAspect.class);
    private long traceId = 0;

    @Pointcut(value = "execution(* com.channabelle.controller..*Controller.*(..))")
    public void BaseControllerLog() {

    }

    @Before("BaseControllerLog()")
    public void doBeforeAdvice(JoinPoint joinPoint) {
        this.traceId = (new Date()).getTime();

        Object[] args = joinPoint.getArgs();
        String argStr = "";
        if(null != args && args.length > 0) {
            for(int m = 0; m < args.length; m++) {
                if(args[m] instanceof JSONObject) {
                    JSONObject j = JSONObject.fromObject(args[m], JsonDateValueProcessor.getJsonConfig());
                    argStr = argStr + String.format("[%d]: %s, ", m, j.toString());
                } else {
                    argStr = argStr + String.format("[%d]: %s, ", m, (null == args[m]) ? "" : args[m].toString());
                }

            }
        }

        log.info(String.format("===> doBeforeAdvice (%d), name: (%s.%s), args: (%s)", this.traceId, joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName(), argStr));
    }

    @AfterReturning(value = "execution(* com.channabelle.controller..*Controller.*(..))", returning = "result")
    public void doAfterReturningAdvice(JoinPoint joinPoint, Object result) {

        JSONObject j = JSONObject.fromObject(result, JsonDateValueProcessor.getJsonConfig());
        log.info(String.format("<=== doAfterReturningAdvice (%d), result: %s", this.traceId, j.toString()));
    }
}
