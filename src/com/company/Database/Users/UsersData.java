package com.company.Database.Users;

import com.company.Model.User;

import javax.swing.*;

public class UsersData {

    DefaultListModel listModel;
    DefaultListModel usernameListModel;

    UsersDataSource usersData;

    /**
     * Constructor initializes the list model that holds userID as Integers and
     * attempts to read any data saved from previous invocations of the
     * application.
     *
     */
    public UsersData() {
        listModel = new DefaultListModel();
        usernameListModel = new DefaultListModel();

        usersData = new JDBCUsersDataSource();

        for (Integer userIDs : usersData.userIDSet()) {
            listModel.addElement(userIDs);
        }

        for (String username: usersData.usernameSet()) {
            usernameListModel.addElement(username);
        }
    }

    /**
     * Adds a User to the database.
     *
     * @param user User to add to the database.
     */
    public void addUser(User user) {
        // check to see if the person is already in the book
        // if not add to the address book and the list model
        if (!usernameListModel.contains(user.getUsername())) {
            usernameListModel.addElement(user.getUsername());
            usersData.addUser(user);
        }
    }


    /**
     * Based on the username of the User in the database, delete the User.
     *
     * @param key userID
     */
    public void remove(Object key) {
        usernameListModel.removeElement(key);
        usersData.deleteUser((Integer) key);
    }

    /**
     * Saves the data in the address book using a persistence
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
     */
    public User get(Object key) {
        return usersData.getUser((String) key);
    }


    /**
     * Accessor for the list model.
     *
     * @return the listModel to display.
     */
    public ListModel getModel() {
        return listModel;
    }

}
