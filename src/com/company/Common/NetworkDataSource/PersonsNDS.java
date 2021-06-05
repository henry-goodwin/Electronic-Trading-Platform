package com.company.Common.NetworkDataSource;

import com.company.Server.Command;
import com.company.Common.DataSource.PersonsDataSource;
import com.company.Common.Model.Person;
import com.company.Server.ServerException;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Set;

public class PersonsNDS implements PersonsDataSource {

    private static final String HOSTNAME = "127.0.0.1";
    private static final int PORT = 10000;

    private Socket socket;
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;


    public PersonsNDS() {
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
     * Sends person to the server so that it can be added to the database
     * @param person Person to add
     * @throws Exception Throws exception if add fails
     */
    @Override
    public void addPerson(Person person) throws Exception {
        try {
            // tell the server to expect a person's details
            outputStream.writeObject(Command.ADD_PERSON);

            // send the actual data
            outputStream.writeObject(person);
            outputStream.flush();

            if (!((Boolean) inputStream.readObject())) throw new Exception("Failed to add person, please try again");

        } catch (IOException e) {
            e.printStackTrace();
            throw new Exception("Failed to add person, please try again");
        }
    }

    /**
     * Gets person from the server
     * @param personID The personID as a Integer to search for.
     * @return Person with the person ID
     * @throws Exception Throw exception if fails
     */
    @Override
    public Person getPerson(Integer personID) throws Exception {
        try {
            outputStream.writeObject(Command.GET_PERSON);
            outputStream.writeObject(personID);
            outputStream.flush();

            if (!((Boolean) inputStream.readObject())) throw new Exception("Failed to get person, please try again");

            Person person = (Person) inputStream.readObject();
            return person;

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            throw new Exception("Failed to get person, please try again");
        }
    }

    @Override
    public void close() {

    }

    /**
     * Gets set of people from the server
     * @return Set of people in the database
     * @throws Exception Throw exception if fails
     */
    @Override
    public Set<Person> personsSet() throws Exception {
        try {
            outputStream.writeObject(Command.GET_PERSON_SET);
            outputStream.flush();
            if (!((Boolean) inputStream.readObject())) throw new ServerException("Failed to get userSet, please try again");
            return (Set<Person>) inputStream.readObject();
        } catch (IOException | ClassNotFoundException | ClassCastException e) {
            e.printStackTrace();
            throw new Exception("Failed to get set, please try again");
        }
    }
}
