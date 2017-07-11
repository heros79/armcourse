package com.course.aca.model.entity;

/**
 * Entity class. Contains User's account-related information.
 *
 * @author Anna
 * @author Norik
 * @author Narek
 */
public class UserInfoEntity {
    private String firstName;
    private String lastName;
    private int roleId;

    public UserInfoEntity() {
    }

    public UserInfoEntity(String firstName, String lastName, int roleId) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.roleId = roleId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }
}
