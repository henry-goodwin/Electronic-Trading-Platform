package com.company.Client.Data;

import com.company.Common.DataSource.UsersDataSource;
import com.company.Common.Model.User;
import com.company.Testing.TestingException;

import javax.swing.*;

public class UsersData {

    private DefaultListModel<User> listModel;
    private DefaultListModel usernameListModel;

    private UsersDataSource usersData;

    /**
     * Constructor initializes the list model that holds userID as Integers and
     * attempts to read any data saved from previous invocations of the
     * application.
     * @throws Exception throws exception if fails to get list
     */
    public UsersData(UsersDataSource dataSource) throws TestingException {
        listModel = new DefaultListModel<User>();
        usernameListModel = new DefaultListModel();

        usersData = dataSource;

        try {
            for (User user : usersData.userSet()) {
                listModel.addElement(user);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new TestingException("Failed");
        }
    }

    /**
     * Adds a User to the database.
     *
     * @param user User to add to the database.
     * @throws Exception throws exception if fails to add user
     */
    public void addUser(User user) throws TestingException {
        // check to see if the person is already in the book
        // if not add to the address book and the list model
        if (!usernameListModel.contains(user.getUsername())) {
            usernameListModel.addElement(user.getUsername());
            try {
                usersData.addUser(user);
            } catch (Exception e) {
                e.printStackTrace();
                throw new TestingException("Failed to add User");
            }
        }
    }

    /**
     * Changes the user's password
     *
     * @param newPassword the new password that the user wants
     * @param userID the ID of the user that wants to change their password
     * @throws Exception throws exception if fails to change password
     * */

    public void changePassword(String newPassword, Integer userID) throws TestingException{
        try {
            usersData.changePassword(newPassword,  userID);
        } catch (Exception e) {
            e.printStackTrace();
            throw new TestingException("Change Password Failed");
        }
    };
    /**
     * Based on the username of the User in the database, delete the User.
     *
     * @param key userID
     */
    public void remove(Object key) throws TestingException {
        usernameListModel.removeElement(key);
        try {
            usersData.deleteUser((Integer) key);
        } catch (Exception e) {
            e.printStackTrace();
            throw new TestingException("Remove failed");
        }
    }

    /**
     * Saves the data in the database using a persistence
     * mechanism.
     */
    public void persist() {
        usersData.close();
    }

    /**
     * Retrieves Users details from the model.
     *
     * @param key the username to retrieve.
     * @return the User object related to the username.
     * @throws Exception throws exception if fails
     */
    public User get(Object key) throws TestingException {
        try {
            return usersData.getUser((String) key);
        } catch (Exception e) {
            e.printStackTrace();
            throw new TestingException("Get Failed");
        }
    }

    /**
     * Checks the availability of a username.
     *
     * @param username the username to check.
     * @return the boolean of username availability.
     */
    public Boolean checkUsernameAvailability(String username) throws TestingException {
        try {
            return usersData.checkUsernameAvailability(username);
        } catch (Exception e) {
            e.printStackTrace();
            throw new TestingException(e.getMessage());
        }
    }



    /**
     * Accessor for the list model.
     *
     * @return the listModel to display.
     */
    public ListModel getModel() {
        return listModel;
    }

    public Boolean login(String username, String hashedPassword) throws TestingException {
        try {
            Boolean login = usersData.login(username, hashedPassword);
            return login;
        } catch (Exception e) {
            e.printStackTrace();
            throw new TestingException("Login Failed");
        }
    }

    /**
     * Checks if password matches password for user with ID
     * @param userID userID to check password
     * @param hashedPassword hashed password to check
     * @return Return true if password match, return false if passwords do not match
     * @throws TestingException Throw exception if fails
     */
    public Boolean checkPassword(Integer userID, String hashedPassword) throws TestingException {
        try {
            return usersData.checkPassword(userID, hashedPassword);
        } catch (Exception e) {
            e.printStackTrace();
            throw new TestingException("Check password failed");
        }
    }

}
