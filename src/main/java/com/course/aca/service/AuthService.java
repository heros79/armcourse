package com.course.aca.service;

import com.course.aca.dao.UserDao;
import com.course.aca.enums.Role;
import com.course.aca.exception.DuplicateUserException;
import com.course.aca.exception.UnauthorizedException;
import com.course.aca.model.dto.LoginRequest;
import com.course.aca.model.dto.UserInfo;
import com.course.aca.model.dto.UserRegistration;
import com.course.aca.model.entity.UserInfoEntity;
import com.course.aca.model.entity.UserRegEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;


/**
 * This class provides public methods associated with authorization and authentication.
 *
 * @author Anna
 * @author Norik
 * @author Narek
 */
@Service
public class AuthService {

    /**
     * Bean of class {@link UserDao }.
     *
     * Initialize using "Autowired" annotation in constructor.
     */
    private final UserDao userDao;

    /**
     * Initializes {@link UserDao}'s bean using "Autowired" annotation.
     *
     * @param userDao Required by "Autowired" annotation.
     */
    @Autowired
    public AuthService(UserDao userDao) {
        this.userDao = userDao;
    }

    /**
     * Public method. Provides user registration functionality.
     *
     * @param userReg {@link UserRegistration} DTO's instance. Stores data about account that is going to be registered.
     *
     * @return Newly registered account's security token.
     *
     * @throws DuplicateUserException Thrown if user with passed email address already exists.
     */
    public String register(UserRegistration userReg) throws DuplicateUserException {
        boolean duplicateUser = userDao.checkIfUserExist(userReg.getEmail());

        if (duplicateUser)
            throw new DuplicateUserException();

        UserRegEntity userRegEntity = new UserRegEntity(userReg.getFirstName(),
                userReg.getLastName(), userReg.getEmail(), userReg.getRoleId(),
                userReg.getPassword());
        return userDao.createUser(userRegEntity);
    }

    /**
     * Public method. Provides user login functionality.
     *
     * @param loginRequest {@link LoginRequest} DTO's instance. Stores username and password of account,
     *      where is requested to log in.
     *
     * @return Authorized users security token.
     *
     * @throws UnauthorizedException Chained up from {@link UserDao#login(String, String)}
     */
    public String login(LoginRequest loginRequest) throws UnauthorizedException {

        return userDao.login(loginRequest.getUsername(), loginRequest.getPassword());
    }

    /**
     * Public method. Provides logout functionality.
     *
     * @param token Security token, which user session is going to be deleted.
     *
     * @return True, if the session is deleted successfully, or false, if it isn't.
     *
     * @throws UnauthorizedException Chained up from {@link UserDao#logout(String)}
     */
    public boolean logout(String token) throws UnauthorizedException {
        return userDao.logout(token);
    }

    /**
     * Public method. Checks if the user with passed token has at least one of roles, passed in role parameter.
     *
     * @param role {@link Role[]}'s instance. Stores all allowed roles.
     * @param token Security token. Used to find the role of user.
     *
     * @return True, if user has at least one role from role array. Otherwise returns false.
     *
     * @throws UnauthorizedException Chained up from {@link UserDao#getRole(String)}
     */
    public boolean hasRole(Role[] role, String token) throws UnauthorizedException {
        int roleId = userDao.getRole(token);

        for (Role r : role) {
            if (r.getRoleId() == roleId) {
                return true;
            }
        }

        return false;
    }

    /**
     * Public method. Provides user-relative information, stored into {@link UserInfo} DTO's instance.
     *
     * @param token Security token. Used to find which user's information is looked for.
     *
     * @return {@link UserInfo} DTO's instance which contains information about user.
     *
     * @throws UnauthorizedException Chained up from {@link UserDao#getUserInfo(String)}
     */
    public UserInfo getUserInfo(String token) throws UnauthorizedException {

        UserInfoEntity entity = userDao.getUserInfo(token);

        return new UserInfo(entity.getFirstName(), entity.getLastName(), entity.getRoleId());
    }

    /**
     * Public method. Validates {@link UserRegistration} DTO's instance.
     *
     * @param user {@link UserRegistration} DTO's instance that is going to be validated.
     *
     * @return {@link Map<String, String>} which contains all found violations after validation, or it's empty,
     *  if no violation is found.
     */
    public Map<String, String> validate(UserRegistration user) {

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();

        Validator validator = factory.getValidator();

        Set<ConstraintViolation<UserRegistration>> set = validator.validate(user);

        Map<String, String> map = new HashMap<>();
        set.forEach(e -> map.put(e.getPropertyPath().toString(), e.getMessageTemplate()));

        return map;
    }
}
