package com.course.aca.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marker for Controller layer's methods. Used to mark courses published by exact teacher.
 *
 * @author Anna
 * @author Norik
 * @author Narek
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface BelongToTeacher {
}
