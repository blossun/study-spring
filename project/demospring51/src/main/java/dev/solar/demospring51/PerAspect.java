package dev.solar.demospring51;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class PerAspect {

    @Around("@annotation(PerLogging)")
    public Object logPerf(ProceedingJoinPoint pjp) throws Throwable {
        long begin = System.currentTimeMillis();
        Object retVal = pjp.proceed(); //메서드 실행
        System.out.println("수행시간 : " + (System.currentTimeMillis() - begin));
        return retVal;
    }
}
