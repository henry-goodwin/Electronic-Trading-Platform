package com.company.Server.Database;

import com.company.Common.DataSource.UsersDataSource;
import com.company.Common.Model.User;
import com.company.Testing.TestingException;
import com.company.Common.PasswordHasher;

import java.sql.*;
import java.util.Set;
import java.util.TreeSet;

public class JDBCUsersDataSource implements UsersDataSource {

    // SQL Statements
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

    private static final String CHANGE_PASSWORD = "UPDATE `cab302`.`Users`" +
            "SET"+
            " `password` = ?" +
            " WHERE `userID` = ?";

    private static final String GET_USERS = "SELECT * FROM `cab302`.`Users` WHERE accountType != 'admin';";
    private static final String GET_USER_FROM_USERID = "SELECT * FROM `cab302`.`Users` WHERE userID=?;";
    private static final String GET_USER_FROM_USERNAME = "SELECT * FROM `cab302`.`Users` WHERE username=?";
    private static final String DELETE_USER_FROM_USERID = "DELETE FROM `cab302`.`Users` WHERE userID=?;";
    private static final String DELETE_USER_FROM_USERNAME = "DELETE FROM `cab302`.`Users` WHERE username=?";
    private static final String GET_USERNAME_COUNT = "SELECT COUNT(*) FROM Users WHERE username=?;";

    // Database Connection
    private Connection connection;

    // Prepared statement for SQL commands
    private PreparedStatement addDefaultAdmin;
    private PreparedStatement addUser;
    private PreparedStatement changePassword;
    private PreparedStatement getUsersList;
    private PreparedStatement getUserFromID;
    private PreparedStatement getUserFromUsername;
    private PreparedStatement deleteUserFromID;
    private PreparedStatement deleteUserFromUsername;
    private PreparedStatement getUsernameCount;

    /**
     * Initializer function
     */
    public JDBCUsersDataSource() {

        // Get database connection
        connection = DBConnector.getInstance();

        try {
            // Create table if not exists
            Statement statement = connection.createStatement();
            statement.execute(CREATE_TABLE);

            // Prepare statements
            addDefaultAdmin = connection.prepareStatement(INSERT_DEFAULT_ADMIN);
            addUser = connection.prepareStatement(INSERT_USER);
            changePassword = connection.prepareStatement(CHANGE_PASSWORD);
            getUsersList = connection.prepareStatement(GET_USERS);
            getUserFromID = connection.prepareStatement(GET_USER_FROM_USERID);
            getUserFromUsername = connection.prepareStatement(GET_USER_FROM_USERNAME);
            deleteUserFromID = connection.prepareStatement(DELETE_USER_FROM_USERID);
            deleteUserFromUsername = connection.prepareStatement(DELETE_USER_FROM_USERNAME);
            getUsernameCount = connection.prepareStatement(GET_USERNAME_COUNT);

            // Check if default admin exists, if it doesn't add to the database
            addDefaultAdmin();

        } catch (SQLException exception) {
            exception.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Add default admin if doesn't exist in the database
     */
    private void addDefaultAdmin() throws TestingException {
        try {
            if (checkUsernameAvailability("admin")) {
                String hashedDefaultPassword = PasswordHasher.hashString(String.valueOf("root"));
                try {
                    addDefaultAdmin.setString(1 ,hashedDefaultPassword);
                    addDefaultAdmin.execute();
                } catch (SQLException exception) {
                    exception.printStackTrace();
                    throw new TestingException("SQL Error");
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new TestingException("Username not available");
        }
    }

    /**
     * Add a user to the database
     * @param user User to add
     * @throws TestingException Throws exception if add fails
     */
    @Override
    public void addUser(User user) throws TestingException {
        if (user.getAccountType() != null && user.getPersonID() != null && user.getUsername() != null && user.getPasswordHash() != null) {
            try {
                addUser.setString(1, user.getAccountType());
                addUser.setInt(2, user.getPersonID());
                addUser.setString(3, user.getUsername());
                addUser.setString(4, user.getPasswordHash());
                addUser.execute();

            } catch (SQLException exception) {
                exception.printStackTrace();
                throw new TestingException("SQL statement failed");
            }
        } else {
            throw new TestingException("Error: Invalid User");
        }
    }

    /**
     * Change password for user
     * @param newPassword the new password that the user wants
     * @param userID the ID of the user that wants to change their password
     * @throws TestingException throw exception if change password fails
     */
    @Override
    public void changePassword(String newPassword, Integer userID) throws TestingException{
        if (userID < 0 || newPassword.equals("")) {
            try {
                changePassword.setString(1, newPassword);
                changePassword.setInt(2, userID);
                changePassword.executeUpdate();

            } catch (SQLException exception) {
                exception.printStackTrace();
                throw new TestingException("SQL Error " + exception.getMessage());
            }
        } else {
            throw new TestingException("Invalid details");
        }
    }

    /**
     * Get user from userID
     * @param userID The userID as a Integer to search for.
     * @return User with the userID
     * @throws TestingException Throw exception if user is unable to be found
     */
    @Override
    public User getUser(Integer userID) throws TestingException {

        if (userID < 0) throw new TestingException("Invalid userID");

        ResultSet resultSet = null;

        try {
            User user = new User();
            getUserFromID.setInt(1,userID);
            resultSet = getUserFromID.executeQuery();

            if (resultSet.next()) {
                user.setUserID(resultSet.getInt("userID"));
                user.setAccountType(resultSet.getString("accountType"));

                // Find and Set Persons Data
                Integer personID = resultSet.getInt("personID");
                user.setPersonID(personID);

                user.setUsername(resultSet.getString("username"));
                user.setPasswordHash(resultSet.getString("password"));
                return user;
            } else {
                throw new TestingException("No User found");
            }

        } catch (SQLException exception) {
            exception.printStackTrace();
            throw new TestingException("SQL Exceptions " + exception.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            throw new TestingException();
        }
    }

    /**
     * Get user from username
     * @param username The username as a String to search for.
     * @return User with the username
     * @throws TestingException Throw exception if get fails
     */
    @Override
    public User getUser(String username) throws TestingException {

        if (!username.matches("^[a-zA-Z0-9]([._-](?![._-])|[a-zA-Z0-9]){3,18}[a-zA-Z0-9]$")) throw new TestingException("Invalid username");

        ResultSet resultSet = null;

        try {
            User user = new User();
            getUserFromUsername.setString(1, username);
            resultSet = getUserFromUsername.executeQuery();

            if (resultSet.next()) {
                user.setUserID(resultSet.getInt("userID"));
                user.setAccountType(resultSet.getString("accountType"));

                // Find and Set Persons Data
                Integer personID = resultSet.getInt("personID");
                user.setPersonID(personID);

                user.setUsername(resultSet.getString("username"));
                user.setPasswordHash(resultSet.getString("password"));
                return user;
            } else {
                throw new TestingException("No User Found");
            }

        } catch (SQLException exception) {

            throw new TestingException("SQL Error");
        } catch (Exception e) {
            e.printStackTrace();
            throw new TestingException();
        }
    }

    /**
     * Check if username is available
     * @param username The username as a String to search for.
     * @return Return true if username is available, return false if username is taken
     * @throws TestingException Throws exception if username is invalid
     */
    @Override
    public Boolean checkUsernameAvailability(String username) throws TestingException {
        if (!username.matches("^[a-zA-Z0-9]([._-](?![._-])|[a-zA-Z0-9]){3,18}[a-zA-Z0-9]$")) throw new TestingException("Invalid username format, please ensure username is between 3-12 characters");

        ResultSet resultSet = null;

        try {
            Integer usernameCount = 0 ;
            getUsernameCount.setString(1, username);
            resultSet = getUsernameCount.executeQuery();

            if (resultSet.next()) {
                usernameCount = resultSet.getInt("COUNT(*)");
            }

            return (usernameCount <= 0);

        } catch (SQLException exception) {
            exception.printStackTrace();
            throw new TestingException("SQL Error");
        }
    }

    /**
     * Delete user with the userID
     * @param userID The userID to delete from the database.
     * @throws TestingException Throw exception is delete fails
     */
    @Override
    public void deleteUser(Integer userID) throws TestingException {
        if (userID < 0) throw new TestingException("Invalid userID");
        try {
            deleteUserFromID.setInt(1, userID);
            deleteUserFromID.executeUpdate();
        } catch (SQLException exception) {
            exception.printStackTrace();
            throw new TestingException("SQL Error");
        }
    }

    /**
     * Delete user with the username
     * @param username The username to delete from the database.
     * @throws Exception Throw exception if delete fails
     */
    @Override
    public void deleteUser(String username) throws TestingException {
        if (!username.matches("^[a-zA-Z0-9]([._-](?![._-])|[a-zA-Z0-9]){3,18}[a-zA-Z0-9]$")) throw new TestingException("Invalid username");

        try {
            deleteUserFromUsername.setString(1, username);
            deleteUserFromUsername.executeUpdate();
        } catch (SQLException exception) {
            exception.printStackTrace();
            throw new TestingException("SQL Error");
        }
    }

    /**
     * Close the connection
     */
    @Override
    public void close() {
        try {
            connection.close();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Gets set of users in the database
     * @return set of Users
     * @throws TestingException Throw exception if get fails
     */
    @Override
    public Set<User> userSet() throws TestingException {
        ResultSet resultSet = null;

        try {
            Set<User> users = new TreeSet<User>();
            resultSet = getUsersList.executeQuery();

            while (resultSet.next()) {
                User user = new User();
                user.setUserID(resultSet.getInt("userID"));
                user.setUsername(resultSet.getString("username"));
                user.setPasswordHash(resultSet.getString("password"));
                user.setAccountType(resultSet.getString("accountType"));

                // Find and Set Persons Data
                Integer personID = resultSet.getInt("personID");
                user.setPersonID(personID);
                users.add(user);
            }
            return users;

        } catch (SQLException exception) {
            exception.printStackTrace();
            throw new TestingException("SQL Error");

        } catch (Exception e) {
            e.printStackTrace();
            throw new TestingException("Invalid data");
        }
    }

    /**
     * Login user
     * @param username username to login
     * @param hashedPassword hashedPassword to login
     * @return Return true if login successful, return false if login failed
     * @throws TestingException Throw exception if error encountered
     */
    @Override
    public boolean login(String username, String hashedPassword) throws TestingException {
        if (!username.matches("^[a-zA-Z0-9]([._-](?![._-])|[a-zA-Z0-9]){3,18}[a-zA-Z0-9]$")) throw new TestingException("Invalid username");
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
        // Check if username exists
        if (userID < 0) throw new TestingException("Invalid userID");
        User user = getUser(userID);

        return user.getPasswordHash().equals(hashedPassword);

    }
}
