package com.company.Common.DataSource;

import java.util.Set;
import com.company.Common.Model.User;

public interface UsersDataSource {

    /**
     * Adds a User to the datebase, if they are not already in the datebase
     *
     * @param user User to add
     */
    void addUser(User user) throws Exception;

    /**
     * Changes the user's password
     *
     * @param newPassword the new password that the user wants
     * @param userID the ID of the user that wants to change their password
     * */


    void changePassword(String newPassword, Integer userID) throws Exception;
    /**
     * Extracts all the details of a User from the database based on the
     * userID passed in.
     *
     * @param userID The userID as a Integer to search for.
     * @return all details in a User object for the UserID
     */
    User getUser(Integer userID) throws Exception;

    /**
     * Checks if a username is available based on the username passed in
     *
     *
     * @param username The username as a String to search for.
     * @return boolean value of username availability
     */
    Boolean checkUsernameAvailability(String username) throws Exception;


    /**
     * Extracts all the details of a User from the database based on the
     * userID passed in.
     *
     * @param username The username as a String to search for.
     * @return all details in a User object for the username
     */
    User getUser(String username) throws Exception;

    /**
     * Deletes a User from the database.
     *
     * @param userID The userID to delete from the database.
     */
    void deleteUser(Integer userID) throws Exception;

    /**
     * Deletes a User from the database.
     *
     * @param username The username to delete from the database.
     */
    void deleteUser(String username) throws Exception;

    /**
     * Finalizes any resources used by the data source and ensures data is
     * persisited.
     */
    void close();

    /**
     * Retrieves a set of usernames from the data source that are used in
     * the database.
     *
     * @return set of usernames.
     */
    Set<User> userSet() throws Exception;

    boolean login(String username, String hashedPassword) throws Exception;

    boolean checkPassword(Integer userID, String hashedPassword) throws Exception;

}
