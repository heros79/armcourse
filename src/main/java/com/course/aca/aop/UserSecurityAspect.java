package com.course.aca.aop;

import com.course.aca.annotation.Secure;
import com.course.aca.enums.Role;
import com.course.aca.exception.UnauthorizedException;
import com.course.aca.service.AuthService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * Spring AOP Aspect class. Used in security handling. Has following methods:
 * {@link UserSecurityAspect#checkRole(ProceedingJoinPoint)}
 *
 * @author Anna
 * @author Norik
 * @author Narek
 */
@Aspect
@Component
public class UserSecurityAspect {

    /**
     * Bean of class {@link AuthService}.
     *
     * Initialize using "Autowired" annotation in constructor.
     */
    private AuthService authService;

    /**
     * Initializes {@link AuthService}'s bean using "Autowired" annotation.
     *
     * @param authService Required by "Autowired" annotation.
     */
    @Autowired
    public UserSecurityAspect(AuthService authService) {
        this.authService = authService;
    }

    /**
     * Provides Security "Role Checking" functionality. Affects all methods in Controller's layer annotated by
     * &#064Secure annotation. Using Reflection API to get annotations.
     *
     * @param joinPoint Spring AOP Join Point.
     * @return Proceeds the Join Point execution. Returns {@link ResponseEntity} with HttpStatus code "OK" if the
     * Authentication token exists or returns {@link ResponseEntity} with HttpStatus code "FORBIDDEN" if it isn't.
     * @throws Throwable Chained up from {@link ProceedingJoinPoint#proceed()}.
     */
    @Around("@annotation(com.course.aca.annotation.Secure)")
    public ResponseEntity checkRole(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] objects = joinPoint.getArgs();
        String token = (String) objects[0];

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Secure secure = method.getAnnotation(Secure.class);
        Role[] roles = secure.role();

        try {
            if (!authService.hasRole(roles, token))
                return new ResponseEntity(HttpStatus.FORBIDDEN);
        }catch (UnauthorizedException e){
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }


        return (ResponseEntity) joinPoint.proceed();
    }


}
