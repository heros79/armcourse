package com.course.aca.controller;

import com.course.aca.exception.DuplicateUserException;
import com.course.aca.exception.UnauthorizedException;
import com.course.aca.model.dto.LoginRequest;
import com.course.aca.model.dto.UserInfo;
import com.course.aca.model.dto.UserRegistration;
import com.course.aca.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * A controller class. Contains endpoints for /auth/* URLs.
 *
 * @author Anna
 * @author Norik
 * @author Narek
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

    /**
     * Service classes' beans.
     *
     * Initialized using "Autowired" annotation in constructor.
     */
    private AuthService authService;

    /**
     * Initializes autowired beans in this class.
     *
     * @param authService
     */
    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * Endpoint of /register URL.
     * Registers new account with given credentials.
     *
     * @param userRegistration DTO class which contains registration credentials information.
     *
     * @return {@link ResponseEntity} with already registered accounts security token inside it's headers. Also can
     * return new {@link ResponseEntity} with HttpStatus code "BAD_REQUEST" if passed user credentials' information
     * is invalid. Can return {@link ResponseEntity} with HttpStatus code "CONFLICT" if account with given Email
     * address is already registered.
     */
    @RequestMapping(value = "/register", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity createUserAccount(@RequestBody UserRegistration userRegistration) {
        Map<String, String> valid = authService.validate(userRegistration);

        if (!valid.isEmpty())
            return new ResponseEntity<>(valid, HttpStatus.BAD_REQUEST);

        try {
            String token = authService.register(userRegistration);
            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", token);

            return ResponseEntity.ok()
                    .headers(headers).build();
        } catch (DuplicateUserException e) {
            e.printStackTrace();
            return new ResponseEntity<>("Account with this Email address is already registered", HttpStatus.CONFLICT);
        }
    }

    /**
     * Endpoint of /login URL.
     * Provide login to system functionality.
     *
     * @param loginRequest DTO class's instance which contains login information.
     *
     * @return {@link ResponseEntity} with HttpStatus code "OK" containing user's security token stored in response's
     * headers or {@link ResponseEntity} with HttpStatus code "UNAUTHORIZED" if given credentials are invalid.
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity login(@RequestBody LoginRequest loginRequest) {

        String token;
        try {
            token = authService.login(loginRequest);
            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", token);

            return ResponseEntity.ok()
                    .headers(headers).build();
        } catch (UnauthorizedException e) {
            e.printStackTrace();
            return new ResponseEntity<>("Invalid credentials", HttpStatus.UNAUTHORIZED);
        }
    }

    /**
     * Endpoint of /logout URL.
     * Deletes specified user's session.
     *
     * @param token User's security token. Identifies which user's session will be deleted.
     *
     * @return {@link ResponseEntity} with HttpStatus code "OK" if logout process completed successfully.
     * Otherwise returns {@link ResponseEntity} with HttpStatus code "UNAUTHORIZED".
     */
    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    public ResponseEntity logout(@RequestHeader("Authorization") String token) {
        boolean isLoggedOut;
        try {
            isLoggedOut = authService.logout(token);
        } catch (UnauthorizedException e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        return isLoggedOut ?
                new ResponseEntity<>("successfully", HttpStatus.OK) :
                new ResponseEntity<>("Invalid credential", HttpStatus.UNAUTHORIZED);
    }

    /**
     * Endpoint of /userInfo URL.
     * Gets user's main information.
     *
     * @param token User's security token. Identifies which user's information is needed.
     *
     * @return {@link UserInfo} wrapped into {@link ResponseEntity} with HttpStatus code "OK" or
     * {@link ResponseEntity} with HttpStatus code "UNAUTHORIZED" if no user found with given
     * security token
     */
    @RequestMapping(value = "/userInfo", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getUserInfo(@RequestHeader("Authorization") String token) {
        UserInfo userInfo;
        try {
            userInfo = authService.getUserInfo(token);
        } catch (UnauthorizedException e) {
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }

        return new ResponseEntity<>(userInfo, HttpStatus.OK);
    }

}
