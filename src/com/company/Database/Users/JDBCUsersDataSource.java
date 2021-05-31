package com.company.Database.Users;

import com.company.Database.DBConnector;
import com.company.Database.Persons.JDBCPersonsDataSource;
import com.company.Database.Persons.PersonsDataSource;
import com.company.Model.Person;
import com.company.Model.User;
import com.company.Utilities.PasswordHasher;

import java.sql.*;
import java.util.Set;
import java.util.TreeSet;

public class JDBCUsersDataSource implements UsersDataSource {

    private static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS `Users` (" +
            "  `userID` int(11) NOT NULL AUTO_INCREMENT," +
            "  `accountType` varchar(255) NOT NULL," +
            "  `personID` int(11) DEFAULT NULL," +
            "  `username` varchar(255) NOT NULL," +
            "  `password` varchar(255) NOT NULL," +
            "  PRIMARY KEY (`userID`)," +
            "  FOREIGN KEY (`personID`)" +
            "  REFERENCES Persons (`personID`)" +
            "  ON DELETE CASCADE" +
            ");";

    private static final String INSERT_DEFAULT_ADMIN = "INSERT INTO `cab302`.`Users`\n" +
            "(\n" +
            "`accountType`,\n" +
            "`username`,\n" +
            "`password`)\n" +
            "VALUES\n" +
            "('Admin', 'admin', ?);";

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

    private static final String GET_USERS = "SELECT * FROM `cab302`.`Users`;";

    private static final String GET_USER_FROM_USERID = "SELECT * FROM `cab302`.`Users` WHERE userID=?;";

    private static final String GET_USER_FROM_USERNAME = "SELECT * FROM `cab302`.`Users` WHERE username=?";

    private static final String DELETE_USER_FROM_USERID = "DELETE FROM `cab302`.`Users` WHERE userID=?;";

    private static final String DELETE_USER_FROM_USERNAME = "DELETE FROM `cab302`.`Users` WHERE username=?";

    private static final String GET_USERNAME_COUNT = "SELECT COUNT(*) FROM Users WHERE username=?;";

    private Connection connection;

    private PreparedStatement addDefaultAdmin;

    private PreparedStatement addUser;

    private PreparedStatement getUsersList;

    private PreparedStatement getUserIDList;

    private PreparedStatement getUserFromID;

    private PreparedStatement getUserFromUsername;

    private PreparedStatement deleteUserFromID;

    private PreparedStatement deleteUserFromUsername;

    private PreparedStatement getUsernameCount;

    public JDBCUsersDataSource() {
        connection = DBConnector.getInstance();

        try {
            Statement statement = connection.createStatement();
            statement.execute(CREATE_TABLE);

            addDefaultAdmin = connection.prepareStatement(INSERT_DEFAULT_ADMIN);
            addUser = connection.prepareStatement(INSERT_USER);
            getUsersList = connection.prepareStatement(GET_USERS);
            getUserIDList = connection.prepareStatement(GET_USERID);
            getUserFromID = connection.prepareStatement(GET_USER_FROM_USERID);
            getUserFromUsername = connection.prepareStatement(GET_USER_FROM_USERNAME);
            deleteUserFromID = connection.prepareStatement(DELETE_USER_FROM_USERID);
            deleteUserFromUsername = connection.prepareStatement(DELETE_USER_FROM_USERNAME);
            getUsernameCount = connection.prepareStatement(GET_USERNAME_COUNT);

            // Check if default admin exists, if it doesn't add to the database
            addDefaultAdmin();

        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    private void addDefaultAdmin() {

        if (checkUsernameAvailability("admin")) {
            String hashedDefaultPassword = PasswordHasher.hashString(String.valueOf("root"));
            try {
                addDefaultAdmin.setString(1 ,hashedDefaultPassword);
                addDefaultAdmin.execute();
            } catch (SQLException exception) {
                exception.printStackTrace();
            }

        }
    }

    @Override
    public void addUser(User user) {
        try {
            addUser.setString(1, user.getAccountType());
            addUser.setInt(2, user.getPersonID());
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

            if (resultSet.next()) {
                user.setEmployeeID(resultSet.getInt("userID"));
                user.setAccountType(resultSet.getString("accountType"));

                // Find and Set Persons Data
                PersonsDataSource personsData = new JDBCPersonsDataSource();
                Integer personID = resultSet.getInt("personID");
                user.setPersonID(personID);

                user.setUsername(resultSet.getString("username"));
                user.setPasswordHash(resultSet.getString("password"));
            }

        } catch (SQLException exception) {
            exception.printStackTrace();
            return null;
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

            if (resultSet.next()) {
                user.setEmployeeID(resultSet.getInt("userID"));
                user.setAccountType(resultSet.getString("accountType"));

                // Find and Set Persons Data
                PersonsDataSource personsData = new JDBCPersonsDataSource();
                Integer personID = resultSet.getInt("personID");
                user.setPersonID(personID);

                user.setUsername(resultSet.getString("username"));
                user.setPasswordHash(resultSet.getString("password"));
            }

        } catch (SQLException exception) {
            // Show Failed Alert
//            exception.printStackTrace();
            return null;
        }

        return user;
    }

    @Override
    public Boolean checkUsernameAvailability(String username) {
        Integer usernameCount = 0 ;
        ResultSet resultSet = null;

        try {
            getUsernameCount.setString(1, username);
            resultSet = getUsernameCount.executeQuery();

            if (resultSet.next()) {
                usernameCount = resultSet.getInt("COUNT(*)");

            }

        } catch (SQLException exception) {
            exception.printStackTrace();
            return false;
        }

        return (usernameCount <= 0);
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
    public Set<User> userSet() {
        Set<User> users = new TreeSet<User>();
        ResultSet resultSet = null;

        try {
            resultSet = getUsersList.executeQuery();

            while (resultSet.next()) {
                User user = new User();
                user.setEmployeeID(resultSet.getInt("userID"));
                user.setUsername(resultSet.getString("username"));
                user.setPasswordHash(resultSet.getString("password"));
                user.setAccountType(resultSet.getString("accountType"));

                // Find and Set Persons Data
                PersonsDataSource personsData = new JDBCPersonsDataSource();
                Integer personID = resultSet.getInt("personID");
                user.setPersonID(personID);
            }

        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        return users;
    }

    @Override
    public boolean login(String username, String hashedPassword) {
        // Check if username exists
        if (!checkUsernameAvailability(username)) {
            User user = getUser(username);
            return user.getPasswordHash().equals(hashedPassword);
        }

        return false;
    }
}
