package com.company.Common.NetworkDataSource;

import com.company.Server.Command;
import com.company.Common.DataSource.UsersDataSource;
import com.company.Common.Model.User;
import com.company.Server.ServerException;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Set;

public class UsersNDS implements UsersDataSource {

    private static final String HOSTNAME = "127.0.0.1";
    private static final int PORT = 10000;

    private Socket socket;
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;

    /**
     * Connect to network data source
     */
    public UsersNDS() {
        try {
            // Persist a single connection through the whole lifetime of the application.
            // We will re-use this same connection/socket, rather than repeatedly opening
            // and closing connections.
            socket = new Socket(HOSTNAME, PORT);
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            outputStream.flush();
            inputStream = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            // If the server connection fails, we're going to throw exceptions
            // whenever the application actually tries to query anything.
            // But it wasn't written to handle this, so make sure your
            // server is running beforehand!
            System.out.println("Failed to connect to server");
        }
    }

    /**
     * Sends user to the server so that it can be added to the database
     * @param user User to add
     * @throws ServerException Throws exception if add fails
     */
    @Override
    public void addUser(User user) throws ServerException {
        try {
            // tell the server to expect a person's details
            outputStream.writeObject(Command.ADD_USER);

            // send the actual data
            outputStream.writeObject(user);
            outputStream.flush();

            if (!((Boolean) inputStream.readObject())) throw new ServerException("Failed to add user, please try again");

        } catch (IOException e) {
            e.printStackTrace();
            throw new ServerException("Failed to add user, please try again");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new ServerException("Failed to add user, please try again");
        }
    }

    /**
     * Gets user from the server
     * @param userID The userID as a Integer to search for.
     * @return User with the userID
     * @throws Exception throw exception if fails
     */
    @Override
    public User getUser(Integer userID) throws Exception {
        try {
            outputStream.writeObject(Command.GET_ID_USER);
            outputStream.writeObject(userID);
            outputStream.flush();

            if (!((Boolean) inputStream.readObject())) throw new Exception("Failed to get user, please try again");

            User user = (User) inputStream.readObject();
            return user;

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            throw new Exception("Failed to add user, please try again");
        }
    }

    /**
     * Sends the change password request to the server
     * @param newPassword the new password that the user wants
     * @param userID the ID of the user that wants to change their password
     * @throws Exception Throw exception if change password fails
     */
    @Override
    public void changePassword(String newPassword, Integer userID) throws Exception {
        try{
            outputStream.writeObject(Command.CHANGE_PASSWORD);
            outputStream.writeObject(newPassword);
            outputStream.writeObject(userID);
            outputStream.flush();

            if (!((Boolean) inputStream.readObject())) throw new Exception("Failed to change password");

        } catch (IOException e) {
            e.printStackTrace();
            throw new Exception("Failed to change password");
        }
    }

    /**
     * Sends check username availability request to the server
     * @param username The username as a String to search for.
     * @return Return true if username is available, return false if username is not available
     * @throws ServerException Throws exception if fails
     */
    @Override
    public Boolean checkUsernameAvailability(String username) throws ServerException {
        try {
            outputStream.writeObject(Command.CHECK_AVAILABILITY);
            outputStream.writeObject(username);
            outputStream.flush();

            if (!((Boolean) inputStream.readObject())) throw new ServerException("Error, invalid username");
            Boolean availability = (Boolean) inputStream.readObject();
            return availability;

        } catch (IOException e) {
            e.printStackTrace();
            throw new ServerException("Error, invalid username");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new ServerException("Error, invalid username");
        }
    }

    /**
     * Sends get user request to the database
     * @param username The username as a String to search for.
     * @return User with the username
     * @throws ServerException throw exception if fails
     */
    @Override
    public User getUser(String username) throws ServerException {
        try {
            outputStream.writeObject(Command.GET_USERNAME_USER);
            outputStream.writeObject(username);
            outputStream.flush();
            if (!((Boolean) inputStream.readObject())) throw new ServerException("Failed to get user");
            User user = (User) inputStream.readObject();
            return user;

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            throw new ServerException("Error, invalid username");
        }
    }

    /**
     * Sends delete user request to the server
     * @param userID The userID to delete from the database.
     * @throws ServerException Throws exception if fails to delete user
     */
    @Override
    public void deleteUser(Integer userID) throws ServerException{
        try {
            outputStream.writeObject(Command.DELETE_USER);
            outputStream.writeObject(userID);
            outputStream.flush();

            if (!((Boolean) inputStream.readObject())) throw new ServerException("Failed to delete user, please try again");

        } catch (IOException e) {
            e.printStackTrace();
            throw new ServerException("Failed to delete user");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new ServerException("Input stream fail");
        }
    }

    @Override
    public void deleteUser(String username) {

    }

    @Override
    public void close() {

    }

    /**
     * Gets set of users from the server
     * @return Set of users in the database
     * @throws ServerException Throw exception if fails
     */
    @Override
    public Set<User> userSet() throws ServerException {
        try {
            outputStream.writeObject(Command.GET_USER_SET);
            outputStream.flush();
            if (!((Boolean) inputStream.readObject())) throw new ServerException("Failed to get userSet, please try again");
            return (Set<User>) inputStream.readObject();
        } catch (IOException | ClassNotFoundException | ClassCastException e) {
            e.printStackTrace();
            throw new ServerException("Error");
        }
    }

    /**
     * Sends login request to the server
     * @param username to login
     * @param hashedPassword to login
     * @return Return true if login successful, return falls if login failed
     * @throws ServerException Throw exception if login fails
     */
    @Override
    public boolean login(String username, String hashedPassword) throws ServerException {
        try {
            outputStream.writeObject(Command.LOGIN);
            outputStream.writeObject(username);
            outputStream.writeObject(hashedPassword);
            outputStream.flush();
            if (!((Boolean) inputStream.readObject())) throw new ServerException("Login Failed, please try again");
            return (Boolean) inputStream.readObject();

        } catch (IOException e) {
            e.printStackTrace();
            throw new ServerException("Login Failed: please try again");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new ServerException("Login Failed: please try again");
        }
    }

    /**
     * Send check password request to the server
     * @param userID userID to check password
     * @param hashedPassword hashed password to check
     * @return return true if passwords match, return false if passwords don't match
     * @throws ServerException Throw exception if fails
     */
    @Override
    public boolean checkPassword(Integer userID, String hashedPassword) throws ServerException {
        try {
            outputStream.writeObject(Command.CHECK_PASSWORD);
            outputStream.writeObject(userID);
            outputStream.writeObject(hashedPassword);
            outputStream.flush();
            if (!((Boolean) inputStream.readObject())) throw new ServerException("Check Password Failed");
            return (Boolean) inputStream.readObject();

        } catch (IOException e) {
            e.printStackTrace();
            throw new ServerException("Check password failed");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new ServerException("Check password failed");
        }
    }
}
