package ru.sbercources.library.aspect;

import java.util.Arrays;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class LoggerAspect {

  @Pointcut("bean(*Controller)")
  public void controllers() {

  }

  @Around("controllers()")
  public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
    log.info("Enter: {}.{}() with arguments = {}",
        joinPoint.getSignature().getDeclaringType(),
        joinPoint.getSignature().getName(),
        Arrays.toString(joinPoint.getArgs()));
    Object result = null;
    try {
      result = joinPoint.proceed();
      log.info("Exit: {}.{}() with result = {}", joinPoint.getSignature().getDeclaringType(), joinPoint.getSignature().getName(), result);

    } catch (Exception e) {
      log.info("Exception: " + e.getMessage());
    }

    return result;
  }
}
