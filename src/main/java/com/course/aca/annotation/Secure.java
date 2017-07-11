package com.course.aca.annotation;

import com.course.aca.enums.Role;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marker for Controller layer's methods. Used in "back-end" security implementation.
 *
 * @author Anna
 * @author Norik
 * @author Narek
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Secure {
    Role[] role(); 
}
