package com.company.Database.Users;

import java.util.Set;
import com.company.Model.User;

public interface UsersDataSource {

    /**
     * Adds a User to the datebase, if they are not already in the datebase
     *
     * @param user User to add
     */
    void addUser(User user);

    /**
     * Extracts all the details of a User from the database based on the
     * userID passed in.
     *
     * @param userID The userID as a Integer to search for.
     * @return all details in a User object for the UserID
     */
    User getUser(Integer userID);


    /**
     * Extracts all the details of a User from the database based on the
     * userID passed in.
     *
     * @param username The username as a String to search for.
     * @return all details in a User object for the username
     */
    User getUser(String username);

    /**
     * Deletes a User from the database.
     *
     * @param userID The userID to delete from the database.
     */
    void deleteUser(Integer userID);

    /**
     * Deletes a User from the database.
     *
     * @param username The username to delete from the database.
     */
    void deleteUser(String username);

    /**
     * Finalizes any resources used by the data source and ensures data is
     * persisited.
     */
    void close();

    /**
     * Retrieves a set of userIDs from the data source that are used in
     * the database.
     *
     * @return set of userIDs.
     */
    Set<Integer> userIDSet();

    /**
     * Retrieves a set of usernames from the data source that are used in
     * the database.
     *
     * @return set of usernames.
     */
    Set<String> usernameSet();

}
