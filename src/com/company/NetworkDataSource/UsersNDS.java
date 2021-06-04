package com.company.NetworkDataSource;

import com.company.Server.Command;
import com.company.Database.Users.UsersDataSource;
import com.company.Model.User;
import com.company.Server.ServerException;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serial;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

public class UsersNDS implements UsersDataSource {

    private static final String HOSTNAME = "127.0.0.1";
    private static final int PORT = 10000;

    private Socket socket;
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;



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

    @Override
    public User getUser(Integer userID) {
        try {
            outputStream.writeObject(Command.GET_ID_USER);
            outputStream.writeObject(userID);
            outputStream.flush();

            User user = (User) inputStream.readObject();
            return user;

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public void changePassword(String newPassword, Integer userID) {
        try{
            outputStream.writeObject(Command.CHANGE_PASSWORD);
            outputStream.writeObject(newPassword);
            outputStream.writeObject(userID);
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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
