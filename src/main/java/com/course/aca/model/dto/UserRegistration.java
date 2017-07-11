package com.course.aca.model.dto;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * Data Transfer Object for User Registration information.
 * Using Hibernate Validation features to provide secured registration without any issues.
 *
 * @author Anna
 * @author Norik
 * @author Narek
 */
public class UserRegistration {

    @NotEmpty(message = "First name can't be empty.")
    @Size(min = 2, max = 32, message = "First name's length must be between 2 and 65 symbols.")
    @Pattern(regexp = "[A-Z][a-z]+", message = "First name must contain only letters and must start with capital letters")
    private String firstName;

    @NotEmpty(message = "Last name can't be empty.")
    @Size(min = 2, max = 32, message = "Last name's length must be between 2 and 65 symbols.")
    @Pattern(regexp = "[A-Z][a-z]+", message = "Last name must contain only letters and must start with capital letters")
    private String lastName;

    @NotEmpty(message = "Email address can't be empty.")
    @Email(message = "Invalid Email address format.")
    @Size(min = 10, max = 64, message = "Email address' length must be between 10 and 64 symbols.")
    private String email;

    @NotEmpty(message = "Password must contain at least 6 symbols.")
    @Size(min = 6, max = 40, message = "Password's length must be between 6 and 40 symbols.")
    private String password;

    @Range(min = 1, max = 2,message = "Role's value must be 1 or 2.")
    private int roleId;

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }
}
