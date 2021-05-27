package com.company.NetworkDataSource;

import com.company.Database.OrganisationUnit.OrganisationUnitDataSource;
import com.company.Model.OrganisationUnit;
import com.company.Model.Person;
import com.company.Utilities.Command;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

public class OrganisationUnitNDS implements OrganisationUnitDataSource {

    private static final String HOSTNAME = "127.0.0.1";
    private static final int PORT = 10000;

    private Socket socket;
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;


    public OrganisationUnitNDS() {
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
    public void addOrganisationUnit(OrganisationUnit organisationUnit) {
        try {
            // tell the server to expect a person's details
            outputStream.writeObject(Command.ADD_ORGANISATION_UNIT);

            // send the actual data
            outputStream.writeObject(organisationUnit);
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public OrganisationUnit getOrganisationUnit(Integer organisationUnitID) {
        try {
            outputStream.writeObject(Command.GET_ORGANISATION_UNIT);
            outputStream.writeObject(organisationUnitID);
            outputStream.flush();

            OrganisationUnit organisationUnit = (OrganisationUnit) inputStream.readObject();
            return organisationUnit;

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public void close() {

    }

    @Override
    public Set<OrganisationUnit> organisationUnitSet() {
        try {
            outputStream.writeObject(Command.GET_ORGANISATION_UNIT_SET);
            outputStream.flush();
            return (Set<OrganisationUnit>) inputStream.readObject();
        } catch (IOException | ClassNotFoundException | ClassCastException e) {
            e.printStackTrace();
            return new HashSet<>();
        }
    }
}
