package com.course.aca.enums;

/**
 * Standard enum with three values: STUDENT(1), TEACHER(2), ADMIN(3).
 *
 * @author Anna
 * @author Norik
 * @author Narek
 */
public enum Role {
    /**
     * Existing instances of enum.
     */
    STUDENT(1),
    TEACHER(2),
    ADMIN(3);


    private int roleId;

    /**
     * Initializes private {@link Role#roleId} variable.
     *
     * @param roleId Stores passed value.
     */
    Role(int roleId){
        this.roleId = roleId;
    }

    public int getRoleId() {
        return roleId;
    }
}
