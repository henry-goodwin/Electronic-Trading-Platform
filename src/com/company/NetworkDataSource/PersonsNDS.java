package com.company.NetworkDataSource;

import com.company.Utilities.Command;
import com.company.Database.Persons.PersonsDataSource;
import com.company.Model.Person;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashSet;
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

    @Override
    public void addPerson(Person person) {
        try {
            // tell the server to expect a person's details
            outputStream.writeObject(Command.ADD_PERSON);

            // send the actual data
            outputStream.writeObject(person);
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Person getPerson(Integer personID) {
        try {
            outputStream.writeObject(Command.GET_PERSON);
            outputStream.writeObject(personID);
            outputStream.flush();

            Person person = (Person) inputStream.readObject();
            return person;

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public void close() {

    }

    @Override
    public Set<Person> personsSet() {
        try {
            outputStream.writeObject(Command.GET_PERSON_SET);
            outputStream.flush();
            return (Set<Person>) inputStream.readObject();
        } catch (IOException | ClassNotFoundException | ClassCastException e) {
            e.printStackTrace();
            return new HashSet<>();
        }
    }
}
