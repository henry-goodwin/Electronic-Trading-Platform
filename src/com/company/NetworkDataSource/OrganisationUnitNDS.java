package com.company.NetworkDataSource;

import com.company.Database.OrganisationUnit.OrganisationUnitDataSource;
import com.company.Model.OrganisationUnit;
import com.company.Server.Command;
import com.company.Server.ServerException;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class OrganisationUnitNDS implements OrganisationUnitDataSource {

    // Database Configuration
    private static final String HOSTNAME = "127.0.0.1";
    private static final int PORT = 10000;

    private Socket socket;
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;


    /**
     * Initialise network data source
     */
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

    /**
     * Sends organisation unit the server so that it can be added to the database
     * @param organisationUnit OrganisationUnit to add
     * @throws Exception Throws exception if add fails
     */
    @Override
    public void addOrganisationUnit(OrganisationUnit organisationUnit) throws Exception {
        try {
            // tell the server to expect a person's details
            outputStream.writeObject(Command.ADD_ORGANISATION_UNIT);
            // send the actual data
            outputStream.writeObject(organisationUnit);
            outputStream.flush();

            if (!((Boolean) inputStream.readObject())) throw new Exception("Failed to add unit, please try again");

        } catch (IOException e) {
            e.printStackTrace();
            throw new Exception("Failed to add unit");
        }
    }

    /**
     * Gets organisation unit from server
      * @param organisationUnitID The organisationUnitID as a Integer to search for.
     * @return Organisation unit from the unitID
     * @throws Exception Throws exception if get fails
     */
    @Override
    public OrganisationUnit getOrganisationUnit(Integer organisationUnitID) throws Exception {
        try {
            outputStream.writeObject(Command.GET_ORGANISATION_UNIT);
            outputStream.writeObject(organisationUnitID);
            outputStream.flush();
            if (!((Boolean) inputStream.readObject())) throw new Exception("Failed to get unit, please try again");
            OrganisationUnit organisationUnit = (OrganisationUnit) inputStream.readObject();
            return organisationUnit;

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            throw new Exception("Failed to get organisation unit");
        }
    }

    /**
     * Close the connection
     */
    @Override
    public void close() { }

    /**
     * Sends organisation unit to the server so that it can be updated in the database
     * @param organisationUnit organisationUnit to update
     * @throws Exception Throw exception if update fails
     */
    @Override
    public void updateOrgUnit(OrganisationUnit organisationUnit) throws Exception {
        try {
            outputStream.writeObject(Command.UPDATE_ORG_UNIT);
            outputStream.writeObject(organisationUnit);
            outputStream.flush();
            if (!((Boolean) inputStream.readObject())) throw new Exception("Failed to update unit, please try again");
        } catch (IOException e) {
            e.printStackTrace();
            throw new Exception("Failed to update organizational unit");
        }
    }

    /**
     * Gets the list of organisation units from the server
     * @return Array List of organisational units
     * @throws Exception Throw exception if update fails
     */
    @Override
    public ArrayList<Object[]> getList() throws Exception {
        try {
            outputStream.writeObject(Command.GET_ORG_LIST);
            outputStream.flush();
            if (!((Boolean) inputStream.readObject())) throw new Exception("Failed to get list of units, please try again");
            return (ArrayList<Object[]>) inputStream.readObject();
        } catch (IOException | ClassNotFoundException | ClassCastException e) {
            e.printStackTrace();
            throw new Exception("Failed to get organisational unit list");
        }
    }
}
