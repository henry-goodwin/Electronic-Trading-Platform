package com.company.Database.Users;

import com.company.Database.DBConnector;
import com.company.Database.Persons.JDBCPersonsDataSource;
import com.company.Database.Persons.PersonsDataSource;
import com.company.Model.Person;
import com.company.Model.User;

import java.sql.*;
import java.util.Set;
import java.util.TreeSet;

public class JDBCUsersDataSource implements UsersDataSource {

    public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS `Users` (\n" +
            "  `userID` int(11) NOT NULL AUTO_INCREMENT,\n" +
            "  `accountType` varchar(45) NOT NULL,\n" +
            "  `personID` int(11) DEFAULT NULL,\n" +
            "  `username` varchar(45) NOT NULL,\n" +
            "  `password` varchar(45) NOT NULL,\n" +
            "  PRIMARY KEY (`userID`),\n" +
            "  UNIQUE KEY `UserID_UNIQUE` (`userID`),\n" +
            "  UNIQUE KEY `username_UNIQUE` (`username`)\n" +
            ");\n";


    private static final String INSERT_USER = "INSERT INTO `cab302`.`Users`\n" +
            "(`accountType`,\n" +
            "`personID`,\n" +
            "`username`,\n" +
            "`password`)\n" +
            "VALUES\n" +
            "(?,\n" +
            "?,\n" +
            "?,\n" +
            "?);\n";

    private static final String GET_USERID = "SELECT `userID` FROM `cab302`.`Users`;";

    private static final String GET_USERNAMES = "SELECT `username` FROM `cab302`.`Users`;";

    private static final String GET_USER_FROM_USERID = "SELECT * FROM `cab302`.`Users` WHERE userID=?;";

    private static final String GET_USER_FROM_USERNAME = "SELECT * FROM `cab302`.`Users` WHERE username=?";

    private static final String DELETE_USER_FROM_USERID = "DELETE FROM `cab302`.`Users` WHERE userID=?;";

    private static final String DELETE_USER_FROM_USERNAME = "DELETE FROM `cab302`.`Users` WHERE username=?";

    private Connection connection;

    private PreparedStatement addUser;

    private PreparedStatement getUserList;

    private PreparedStatement getUsernameList;

    private PreparedStatement getUserFromID;

    private PreparedStatement getUserFromUsername;

    private PreparedStatement deleteUserFromID;

    private PreparedStatement deleteUserFromUsername;

    public JDBCUsersDataSource() {
        connection = DBConnector.getInstance();

        try {
            Statement statement = connection.createStatement();
            statement.execute(CREATE_TABLE);
            /* BEGIN MISSING CODE */
            addUser = connection.prepareStatement(INSERT_USER);
            getUsernameList = connection.prepareStatement(GET_USERNAMES);
            getUserList = connection.prepareStatement(GET_USERID);
            getUserFromID = connection.prepareStatement(GET_USER_FROM_USERID);
            getUserFromUsername = connection.prepareStatement(GET_USER_FROM_USERNAME);
            deleteUserFromID = connection.prepareStatement(DELETE_USER_FROM_USERID);
            deleteUserFromUsername = connection.prepareStatement(DELETE_USER_FROM_USERNAME);

        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void addUser(User user) {
        try {
            addUser.setString(1, user.getAccountType());
            addUser.setInt(2, user.getPerson().getPersonID());
            addUser.setString(3, user.getUsername());
            addUser.setString(4, user.getPasswordHash());

            addUser.execute();

        } catch (SQLException exception) {
            exception.printStackTrace();
        }

    }

    @Override
    public User getUser(Integer userID) {
        User user = new User();
        ResultSet resultSet = null;

        try {

            getUserFromID.setInt(1,userID);
            resultSet = getUserFromID.executeQuery();
            resultSet.next();
            user.setEmployeeID(resultSet.getInt("userID"));
            user.setAccountType(resultSet.getString("accountType"));

            // Find and Set Persons Data
            PersonsDataSource personsData = new JDBCPersonsDataSource();
            Person person = personsData.getPerson(resultSet.getInt("personID"));
            user.setPerson(person);

            user.setUsername(resultSet.getString("username"));
            user.setPasswordHash(resultSet.getString("password"));


        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        return user;
    }

    @Override
    public User getUser(String username) {
        User user = new User();
        ResultSet resultSet = null;

        try {

            getUserFromUsername.setString(1, username);
            resultSet = getUserFromUsername.executeQuery();
            resultSet.next();
            user.setEmployeeID(resultSet.getInt("userID"));
            user.setAccountType(resultSet.getString("accountType"));

            // Find and Set Persons Data
            PersonsDataSource personsData = new JDBCPersonsDataSource();
            Person person = personsData.getPerson(resultSet.getInt("personID"));
            user.setPerson(person);

            user.setUsername(resultSet.getString("username"));
            user.setPasswordHash(resultSet.getString("password"));


        } catch (SQLException exception) {
            // Show Failed Alert
//            exception.printStackTrace();
        }

        return user;
    }

    @Override
    public void deleteUser(Integer userID) {
        try {
            deleteUserFromID.setInt(1, userID);
            deleteUserFromID.executeUpdate();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void deleteUser(String username) {
        try {
            deleteUserFromUsername.setString(1, username);
            deleteUserFromUsername.executeUpdate();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void close() {
        try {
            connection.close();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public Set<Integer> userIDSet() {
        Set<Integer> userIDs = new TreeSet<Integer>();
        ResultSet resultSet = null;

        try {
            resultSet = getUserList.executeQuery();

            while (resultSet.next()) {
                userIDs.add(resultSet.getInt("userID"));
            }

        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        return userIDs;

    }

    @Override
    public Set<String> usernameSet() {
        Set<String> usernames = new TreeSet<String>();
        ResultSet resultSet = null;

        try {
            resultSet = getUsernameList.executeQuery();

            while (resultSet.next()) {
                usernames.add(resultSet.getString("username"));
            }

        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        return usernames;
    }
}
