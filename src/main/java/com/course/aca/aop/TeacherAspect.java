package com.course.aca.aop;

import com.course.aca.enums.Role;
import com.course.aca.exception.IllegalParameterException;
import com.course.aca.exception.UnauthorizedException;
import com.course.aca.service.AuthService;
import com.course.aca.service.TeacherService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * Spring AOP Aspect class. Used in security handling. Has following methods:
 * {@link TeacherAspect#belongTo(ProceedingJoinPoint)}.
 *
 * @author Anna
 * @author Norik
 * @author Narek
 */
@Aspect
@Component
public class TeacherAspect {

    /**
     * Bean of class {@link TeacherService}.
     *
     * Initialize using "Autowired" annotation in constructor.
     */
    private TeacherService teacherService;
    /**
     * Bean of class {@link AuthService}.
     *
     * Initialize using "Autowired" annotation in constructor.
     */
    private AuthService authService;

    /**
     * Initializes {@link TeacherService}'s bean using "Autowired" annotation.
     * Initializes {@link AuthService}'s bean using "Autowired" annotation.
     *
     * @param teacherService Required by "Autowired" annotation.
     * @param authService Required by "Autowired" annotation.
     */
    @Autowired
    public TeacherAspect(TeacherService teacherService, AuthService authService) {
        this.teacherService = teacherService;
        this.authService = authService;
    }

    /**
     * Provides Security "Role Checking" and "teacher-course belonging" functionalities. Affects all methods in
     * Controller's layer annotated by &#064BelongToTeacher annotation. Using Reflection API to get annotations.
     *
     * @param joinPoint Spring AOP Join Point.
     * @return Proceeds the Join Point execution. Returns {@link ResponseEntity} with HttpStatus code "OK" if the
     * Authentication token exists and the requested course belongs to current teacher.
     * Returns {@link ResponseEntity} with HttpStatus code "FORBIDDEN" if at least one of this conditions is false.
     * @throws Throwable Chained up from {@link ProceedingJoinPoint#proceed()}.
     */
    @Around("@annotation(com.course.aca.annotation.BelongToTeacher)")
    public ResponseEntity belongTo(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] objects = joinPoint.getArgs();
        String authToken = (String) objects[0];
        int courseId = (int) objects[1];

        try {
            if (!authService.hasRole(new Role[]{Role.TEACHER}, authToken))
                return new ResponseEntity(HttpStatus.FORBIDDEN);

            if (!teacherService.isCourseBelongTeacher(courseId, authToken)) {
                return new ResponseEntity<>("Course doesn't belong to this teacher", HttpStatus.FORBIDDEN);
            }
        } catch (IllegalParameterException e) {
            e.printStackTrace();
            return new ResponseEntity<>("", HttpStatus.BAD_REQUEST);
        } catch (UnauthorizedException e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        return (ResponseEntity) joinPoint.proceed();
    }
}
