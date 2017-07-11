package com.course.aca.dao;

import com.course.aca.exception.UnauthorizedException;
import com.course.aca.model.entity.UserInfoEntity;
import com.course.aca.model.entity.UserRegEntity;
import com.course.aca.model.mapper.UserInfoMapper;
import com.mysql.jdbc.Statement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.sql.PreparedStatement;

/**
 * Data Access Object. Contains public methods which allow to connect to DataBase, Create, Read, Update or
 * delete data from there.
 *
 * @author Anna
 * @author Norik
 * @author Narek
 */
@Repository
public class UserDao {

    /**
     * Instance of Spring JDBC framework's @{@link JdbcTemplate} class.
     *
     * It's initialized in public constructor.
     */
    private final JdbcTemplate jdbcTemplate;

    /**
     * The Mapper bean that is used in DB queries.
     */
    private final UserInfoMapper userInfoMapper;

    /**
     * Initializes autowired beans in this class.
     *
     * @param jdbcTemplate
     */
    @Autowired
    public UserDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.userInfoMapper = new UserInfoMapper();
    }

    /**
     * Gets user's information from DataBase.
     *
     * @param token User's Security Token. Identifies which user's information is needed.
     *
     * @return {@link UserInfoEntity} Entity, that contains needed information.
     *
     * @throws UnauthorizedException Thrown when no user is authorized with such credentials.
     */
    public UserInfoEntity getUserInfo(String token) throws UnauthorizedException {

        UserInfoEntity entity;

        final String query = "SELECT user.firstName, user.lastName,roleId FROM " +
                " user, user_session WHERE user.id = user_session.userId AND user_session.token = ?";

        try {
            entity = jdbcTemplate.queryForObject(query, userInfoMapper, token);
        } catch (DataAccessException e) {
            throw new UnauthorizedException();
        }

        return entity;
    }

    /**
     * Adds new users information into DataBase.
     *
     * @param userRegEntity Entity that contains all needed information for new user registration.
     *
     * @return {@link String} Newly added user's security token.
     */
    @Transactional
    public String createUser(UserRegEntity userRegEntity) {
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();


        final String query = "INSERT INTO user (firstName, lastName, email," +
                " password, roleId) VALUES (?,?,?,?,?) ";

        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

            statement.setString(1, userRegEntity.getFirstName());
            statement.setString(2, userRegEntity.getLastName());
            statement.setString(3, userRegEntity.getEmail());
            statement.setString(4, userRegEntity.getPassword());
            statement.setInt(5, userRegEntity.getRoleId());

            return statement;
        }, keyHolder);

        int userId = keyHolder.getKey().intValue();
        String token = generateToken();

        setToken(userId, token);

        return token;
    }

    /**
     * Sets user's security token in DataBase.
     *
     * @param userId Identifies which user's security token will be added.
     * @param token The security token, that will be mapped to user.
     */
    private void setToken(int userId, String token) {

        String query = "INSERT INTO user_session(userId,token) VALUE (?,?)";
        jdbcTemplate.update(query, userId, token);
    }

    /**
     * Checks if user whit given Email address exists or not.
     *
     * @param email Email address, that will be checked.
     *
     * @return True, if user with given Email address already exists in DataBase. Otherwise returns False.
     */
    public boolean checkIfUserExist(String email) {
        String query = "SELECT email FROM user  WHERE email = ?";
        try {
            String result = jdbcTemplate.queryForObject(query, String.class, email);
            return email.equals(result);
        } catch (EmptyResultDataAccessException e) {
            return false;
        }
    }

    /**
     * Generates new Security Token for user and returns it.
     *
     * @return Newly generated Security Token.
     */
    // TODO: 7/1/2017 Why is this method here???
    private String generateToken() {
        SecureRandom secureRandom = new SecureRandom();
        return new BigInteger(250, secureRandom).toString(32);
    }

    /**
     * Set new Security Token for specified user into DataBase and returns it.
     *
     * @param username Specifies which user is goint to log in.
     * @param password User's password.
     *
     * @return Newly generated Security Token for specified user
     *
     * @throws UnauthorizedException Thrown when no user registered with such credentials.
     */
    public String login(String username, String password) throws UnauthorizedException {
        String query = "SELECT user.id FROM  user WHERE email = ? AND password = ?";

        Integer id;
        try {
            id = jdbcTemplate.queryForObject(query, Integer.class, username, password);
            deleteUserSession(id);
            String token = generateToken();
            setToken(id, token);
            return token;
        } catch (DataAccessException e) {
            throw new UnauthorizedException();
        }
    }

    /**
     * Transactional method. Delete's specified user's session information from DataBase.
     *
     * @param token User's Security Token. Identifies which user's session information will be deleted.
     *
     * @return True, if the session is deleted successfully. Otherwise returns False.
     *
     * @throws UnauthorizedException Thrown when no user is found with such token.
     */
    @Transactional
    public boolean logout(String token) throws UnauthorizedException {
        String query = "DELETE FROM user_session WHERE token = ?";
        try {
            return jdbcTemplate.update(query, token) == 1;
        } catch (DataAccessException e) {
            throw new UnauthorizedException();
        }
    }

    /**
     * Gets specified users current role from DataBase.
     *
     * @param token User's security token. Identifies which user's role is needed.
     *
     * @return Needed role's id.
     *
     * @throws UnauthorizedException Thrown when no user is found with such token.
     */
    public int getRole(String token) throws UnauthorizedException {
        String query = "SELECT roleId FROM user WHERE id = (SELECT userId FROM user_session WHERE token = ?)";

        Integer roleId;
        try {
            roleId = jdbcTemplate.queryForObject(query, Integer.class, token);
        } catch (DataAccessException e) {
            throw new UnauthorizedException();
        }

        return roleId;
    }

    /**
     * Transactional method. Deletes user's session information from DataBase.
     *
     * @param userId Identifies which user's session information will be deleted.
     */
    @Transactional
    private void deleteUserSession(int userId) {
        String query = "DELETE FROM user_session WHERE userId = ?";

        try {
            jdbcTemplate.update(query, userId);
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets user's id from DataBase using his current security token.
     *
     * @param token User's security token. Identifies which user's id is required.
     *
     * @return Needed user's Id.
     */
    public int getUserId(String token) {
        String query = " SELECT userId FROM user_session WHERE token = ?";

        return jdbcTemplate.queryForObject(query, Integer.class, token);
    }
}
