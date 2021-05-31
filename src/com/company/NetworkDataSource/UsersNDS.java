package com.company.NetworkDataSource;

import com.company.Server.Command;
import com.company.Database.Users.UsersDataSource;
import com.company.Model.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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
    public void addUser(User user) {
        try {
            // tell the server to expect a person's details
            outputStream.writeObject(Command.ADD_USER);

            // send the actual data
            outputStream.writeObject(user);
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
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
    public Boolean checkUsernameAvailability(String username) {
        try {
            outputStream.writeObject(Command.CHECK_AVAILABILITY);
            outputStream.writeObject(username);
            outputStream.flush();

            Boolean availability = (Boolean) inputStream.readObject();
            return availability;

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


        return null;
    }

    @Override
    public User getUser(String username) {
        try {
            outputStream.writeObject(Command.GET_USERNAME_USER);
            outputStream.writeObject(username);
            outputStream.flush();

            User user = (User) inputStream.readObject();
            return user;

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public void deleteUser(Integer userID) {
        try {
            outputStream.writeObject(Command.DELETE_USER);
            outputStream.writeObject(userID);
            outputStream.flush();

        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    @Override
    public void deleteUser(String username) {

    }

    @Override
    public void close() {

    }

    @Override
    public Set<User> userSet() {
        try {
            outputStream.writeObject(Command.GET_USER_SET);
            outputStream.flush();
            return (Set<User>) inputStream.readObject();
        } catch (IOException | ClassNotFoundException | ClassCastException e) {
            e.printStackTrace();
            return new HashSet<>();
        }
    }

    @Override
    public boolean login(String username, String hashedPassword) {
        try {
            outputStream.writeObject(Command.LOGIN);
            outputStream.writeObject(username);
            outputStream.writeObject(hashedPassword);
            outputStream.flush();

            return (Boolean) inputStream.readObject();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return false;
    }
}
