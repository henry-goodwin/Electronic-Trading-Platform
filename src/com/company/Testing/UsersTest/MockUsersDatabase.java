package com.company.Testing.UsersTest;

import com.company.Common.DataSource.UsersDataSource;
import com.company.Common.Model.User;
import com.company.Testing.TestingException;
import com.company.Common.PasswordHasher;

import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public class MockUsersDatabase implements UsersDataSource {

    TreeMap<Integer, User> dataUserID;
    TreeMap<String, User> dataUsername;


    /**
     * Initializer function
     */
    public MockUsersDatabase() {
        dataUserID = new TreeMap<Integer, User>();
        dataUsername = new TreeMap<String, User>();
        try {
            dataUserID.put(1, new User(1, "Standard", 2, "davey", PasswordHasher.hashString("deadly")));
            dataUserID.put(2, new User(2, "Admin", 4, "hughy", PasswordHasher.hashString("chick")));

            dataUsername.put("davey", new User(1, "Standard", 2, "davey", PasswordHasher.hashString("deadly")));
            dataUsername.put("hughy", new User(2, "Admin", 4, "hughy", PasswordHasher.hashString("chick")));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Adds user to the database
     * @param user User to add
     * @throws Exception Throws exception if user is invalid
     */
    @Override
    public void addUser(User user) throws Exception {
        if (user.getUserID() != null && user.getAccountType() != null && user.getPersonID() != null && user.getUsername() != null && user.getPasswordHash() != null) {
            dataUserID.put(user.getUserID(), user);
            dataUsername.put(user.getUsername(), user);
        } else {
            throw new Exception("Error: Invalid User");
        }
    }

    /**
     * Change users password
     * @param newPassword the new hashed password that the user wants
     * @param userID the ID of the user that wants to change their password
     * @throws Exception throws exception if newPassword or userID is invalid
     */
    @Override
    public void changePassword(String newPassword, Integer userID) throws Exception {
        if (userID < 0 || newPassword.equals("")) {
            throw new Exception("Invalid details");
        } else {
            User user = dataUserID.get(userID);
            dataUserID.get(userID).setPasswordHash(newPassword);
            dataUsername.get(user.getUsername()).setPasswordHash(newPassword);
        }
    }

    /**
     * Get user from userID
     * @param userID The userID as a Integer to search for.
     * @return User
     * @throws Exception throw exception if userID is invalid
     */
    @Override
    public User getUser(Integer userID) throws Exception {
        if (userID < 0) throw new Exception("Invalid userID");

        User user = dataUserID.get(userID);
        return user;
    }

    /**
     * Check if username is available
     * @param username The username as a String to search for.
     * @return Return true if username is available, return false if username is taken
     * @throws Exception if username is invalid
     */
    @Override
    public Boolean checkUsernameAvailability(String username) throws Exception {
        if (!username.matches("^[a-zA-Z0-9]([._-](?![._-])|[a-zA-Z0-9]){3,18}[a-zA-Z0-9]$")) throw new Exception("Invalid username");
        Set<String> usernames = dataUsername.keySet();

        for (String usernameSet: usernames) {
            if (usernameSet.equals(username)) return false;
        }

        return true;
    }


    /**
     * Get user form username
     * @param username The username as a String to search for.
     * @return User with the username
     * @throws Exception Throw exception if username is invalid
     */
    @Override
    public User getUser(String username) throws Exception {
        if (!username.matches("^[a-zA-Z0-9]([._-](?![._-])|[a-zA-Z0-9]){3,18}[a-zA-Z0-9]$")) throw new Exception("Invalid username");

        User user = dataUsername.get(username);
        return user;

    }

    /**
     * Delete user with the userID
     * @param userID The userID to delete from the database.
     * @throws Exception Throw exception is delete fails
     */
    @Override
    public void deleteUser(Integer userID) throws Exception {
        if (userID < 0) throw new Exception("Invalid userID");

        User userToRemove = dataUserID.get(userID);
        dataUserID.remove(userID);
        dataUserID.remove(userToRemove.getUserID());

    }

    /**
     * Delete user with the username
     * @param username The username to delete from the database.
     * @throws Exception Throw exception if delete fails
     */
    @Override
    public void deleteUser(String username) throws Exception {
        if (!username.matches("^[a-zA-Z0-9]([._-](?![._-])|[a-zA-Z0-9]){3,18}[a-zA-Z0-9]$")) throw new Exception("Invalid username");

        User userToRemove = dataUsername.get(username);
        dataUsername.remove(username);
        dataUserID.remove(userToRemove.getUserID());
    }

    /**
     * Close the connection
     */
    @Override
    public void close() {

    }

    /**
     * Gets set of users in the database
     * @return set of Users
     * @throws TestingException Throw exception if get fails
     */
    @Override
    public Set<User> userSet() throws Exception {
        Set<User> userSet = new TreeSet<User>();

        for(Integer key: dataUserID.keySet()) {
            userSet.add(dataUserID.get(key));
        }
        return userSet;
    }


    /**
     * Login user
     * @param username username to login
     * @param hashedPassword hashedPassword to login
     * @return Return true if login successful, return false if login failed
     * @throws TestingException Throw exception if error encountered
     */
    @Override
    public boolean login(String username, String hashedPassword) throws Exception {
        if (!username.matches("^[a-zA-Z0-9]([._-](?![._-])|[a-zA-Z0-9]){3,18}[a-zA-Z0-9]$")) throw new Exception("Invalid username");
        if (!checkUsernameAvailability(username)) {
            User user = getUser(username);
            return user.getPasswordHash().equals(hashedPassword);
        } else {
            throw new TestingException("Username does not exist");
        }
    }

    /**
     * Check if password exists
     * @param userID userID to check
     * @param hashedPassword hashedPassword to check
     * @return Return true if passwords are equal, return false if password are not equal
     * @throws Exception Throw exception if userID isnt valid
     */
    @Override
    public boolean checkPassword(Integer userID, String hashedPassword) throws TestingException {
        if (userID < 0) throw new TestingException("Invalid userID");
        User user = dataUserID.get(userID);
        return user.getPasswordHash().equals(hashedPassword);
    }
}
