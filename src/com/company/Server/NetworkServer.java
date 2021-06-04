package com.company.Server;

import com.company.Database.Assets.AssetDataSource;
import com.company.Database.Assets.JDBCAssetDataSource;
import com.company.Database.Bids.BidDataSource;
import com.company.Database.Bids.JDBCBidDataSource;
import com.company.Database.OrgUnitAssets.JDBCOrgAssetDataSource;
import com.company.Database.OrgUnitAssets.OrgUnitAssetDataSource;
import com.company.Database.OrgUnitEmployees.JDBCOrgUnitEmployeeDataSource;
import com.company.Database.OrgUnitEmployees.OrgUnitEmployeeDataSource;
import com.company.Database.OrganisationUnit.JDBCOrganisationUnitDataSource;
import com.company.Database.OrganisationUnit.OrganisationUnitDataSource;
import com.company.Model.*;
import com.company.Database.Persons.JDBCPersonsDataSource;
import com.company.Database.Persons.PersonsDataSource;
import com.company.Database.Users.JDBCUsersDataSource;
import com.company.Database.Users.UsersDataSource;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicBoolean;

public class NetworkServer {
    private static final int PORT = 10000;

    /**
     * this is the timeout inbetween accepting clients, not reading from the socket itself.
     */
    private static final int SOCKET_ACCEPT_TIMEOUT = 100;

    /**
     * This timeout is used for the actual clients.
     */
    private static final int SOCKET_READ_TIMEOUT = 5000;

    private AtomicBoolean running = new AtomicBoolean(true);

    /**
     * The connection to the database where everything is stored.
     */
    private UsersDataSource usersDatabase;
    private PersonsDataSource personsDatabase;
    private OrganisationUnitDataSource organisationUnitDatabase;
    private AssetDataSource assetDatabase;
    private OrgUnitAssetDataSource orgAssetDatabase;
    private BidDataSource bidDatabase;
    private OrgUnitEmployeeDataSource unitEmployeeDatabase;

    /**
     * Handles the connection received from ServerSocket.
     * Does not return until the client disconnects.
     * @param socket The socket used to communicate with the currently connected client
     */
    private void handleConnection(Socket socket) {
        try {
            /**
             * We create the streams once at connection time, and re-use them until the client disconnects.
             * This **must** be in the opposite order to the client, because creating an ObjectInputStream
             * reads data, and an ObjectOutputStream writes data.
             */
            final ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
            final ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());

            /**
             * while(true) here might seem a bit confusing - why do we have an infinite loop?
             * That's because we don't want to exit until the client disconnects, and when they do, readObject()
             * will throw an IOException, which will cause this method to exit. Another option could be to have
             * a "close" message/command sent by the client, but if the client closes improperly, or they lose
             * their network connection, it's not going to get sent anyway.
             */
            while (true) {
                try {
                    /**
                     * Read the command, this tells us what to send the client back.
                     * If the client has disconnected, this will throw an exception.
                     */
                    final Command command = (Command) inputStream.readObject();
                    handleCommand(socket, inputStream, outputStream, command);
                } catch (SocketTimeoutException e) {
                    /**
                     * We catch SocketTimeoutExceptions, because that just means the client hasn't sent
                     * any new requests. We don't want to disconnect them otherwise. Another way to
                     * check if they're "still there would be with ping/pong commands.
                     */
                    continue;
                }
            }
        } catch (IOException | ClassCastException | ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println(String.format("Connection %s closed", socket.toString()));
        }
    }

    /**
     * Handles a request from the client.
     * @param socket socket for the client
     * @param inputStream input stream to read objects from
     * @param outputStream output stream to write objects to
     * @param command command we're handling
     * @throws IOException if the client has disconnected
     * @throws ClassNotFoundException if the client sends an invalid object
     * @throws ServerException if database query fails
     */
    private void handleCommand(Socket socket, ObjectInputStream inputStream, ObjectOutputStream outputStream,
                               Command command) throws IOException, ClassNotFoundException {
        /**
         * Remember this is happening on separate threads for each client. Therefore access to the database
         * must be thread-safe in some way. The easiest way to achieve thread safety is to just put a giant
         * lock around all database operations, in this case with a synchronized block on the database object.
         */
        switch (command) {

            case ADD_USER: {
                // client is sending us a new User
                final User user = (User) inputStream.readObject();
                synchronized (usersDatabase) {
                    try {
                        usersDatabase.addUser(user);
                        outputStream.writeObject(true); // Write true if add user is successful
                    } catch (Exception e) {
                        outputStream.writeObject(false);
                    }
                }
                outputStream.flush();
            }
            break;

            case GET_ID_USER: {
                final Integer userID = (Integer) inputStream.readObject();
                synchronized (usersDatabase) {
                    final User user;
                    try {
                        user = usersDatabase.getUser(userID);
                        outputStream.writeObject(true);
                        outputStream.writeObject(user);
                    } catch (Exception e) {
                        e.printStackTrace();
                        outputStream.writeObject(false);
                    }
                }
                outputStream.flush();
            }
            break;

            case GET_USERNAME_USER: {
                final String username = (String) inputStream.readObject();
                synchronized (usersDatabase) {
                    final User user;
                    try {
                        user = usersDatabase.getUser(username);
                        outputStream.writeObject(true);
                        outputStream.writeObject(user);

                    } catch (Exception e) {
                        e.printStackTrace();
                        outputStream.writeObject(false);
                    }
                }
                outputStream.flush();
            }
            break;
            case CHECK_AVAILABILITY: {
                final String username = (String) inputStream.readObject();
                synchronized (usersDatabase) {
                    final Boolean result;
                    try {
                        result = usersDatabase.checkUsernameAvailability(username);
                        outputStream.writeObject(true);
                        outputStream.writeObject(result);

                    } catch (Exception e) {
                        outputStream.writeObject(false);
                        e.printStackTrace();
                    }
                }
                outputStream.flush();
            }
            break;
            case DELETE_USER: {
                // one parameter - the person's name
                final Integer userID = inputStream.readInt();
                synchronized (usersDatabase) {
                    try {
                        usersDatabase.deleteUser(userID);
                        outputStream.writeObject(true); // Write true if add user is successful

                    } catch (Exception e) {
                        e.printStackTrace();
                        outputStream.writeObject(false); // Write false if fails
                    }
                }
                outputStream.flush();
            }
            break;

            case GET_USER_SET: {
                synchronized (usersDatabase) {
                    try {
                        Set<User> userSet = usersDatabase.userSet();
                        outputStream.writeObject(true); // Write true if add user is successful
                        outputStream.writeObject(userSet);

                    } catch (Exception e) {
                        e.printStackTrace();
                        outputStream.writeObject(false); // Write false if fails

                    }
                }
                outputStream.flush();
            }
            break;

            case ADD_PERSON: {
                final Person person = (Person) inputStream.readObject();
                synchronized (personsDatabase) {
                    try {
                        personsDatabase.addPerson(person);
                        outputStream.writeObject(true);
                    } catch (Exception e) {
                        e.printStackTrace();
                        outputStream.writeObject(false);
                    }
                }
                outputStream.flush();
            }
            break;

            case GET_PERSON: {
                final Integer personID = (Integer) inputStream.readObject();
                synchronized (personsDatabase) {
                    final Person person;
                    try {
                        person = personsDatabase.getPerson(personID);
                        outputStream.writeObject(true);
                        outputStream.writeObject(person);

                    } catch (Exception e) {
                        e.printStackTrace();
                        outputStream.writeObject(false);
                    }
                }
                outputStream.flush();
            }
            break;

            case GET_PERSON_SET: {
                synchronized (personsDatabase) {
                    try {
                        Set<Person> personSet = personsDatabase.personsSet();
                        outputStream.writeObject(true);
                        outputStream.writeObject(personSet);
                    } catch (Exception e) {
                        e.printStackTrace();
                        outputStream.writeObject(false);
                    }
                }
                outputStream.flush();
            }
            break;

            case ADD_ORGANISATION_UNIT: {
                final OrganisationUnit organisationUnit = (OrganisationUnit) inputStream.readObject();
                synchronized (organisationUnitDatabase) {
                    try {
                        organisationUnitDatabase.addOrganisationUnit(organisationUnit);
                        outputStream.writeObject(true); // Write true if add user is successful

                    } catch (Exception e) {
                        e.printStackTrace();
                        outputStream.writeObject(false); // Write false if fails
                    }
                }
                outputStream.flush();
            }
            break;

            case GET_ORGANISATION_UNIT: {
                final Integer organisationalUnitID = (Integer) inputStream.readObject();
                synchronized (organisationUnitDatabase) {
                    final OrganisationUnit organisationUnit;
                    try {
                        organisationUnit = organisationUnitDatabase.getOrganisationUnit(organisationalUnitID);
                        outputStream.writeObject(true); // Write true if add user is successful
                        outputStream.writeObject(organisationUnit);
                    } catch (Exception e) {
                        e.printStackTrace();
                        outputStream.writeObject(false); // Write false if fails
                    }
                }
                outputStream.flush();
            }
            break;

            case ADD_ASSET: {
                final Asset asset = (Asset) inputStream.readObject();
                synchronized (assetDatabase) {
                    try {
                        assetDatabase.addAsset(asset);
                        outputStream.writeObject(true); // Write true if add user is successful
                    } catch (Exception e) {
                        e.printStackTrace();
                        outputStream.writeObject(false);
                    }
                }
                outputStream.flush();
            }
            break;

            case UPDATE_ASSET: {

                final Integer assetID = (Integer) inputStream.readObject();
                final String name = (String) inputStream.readObject();

                synchronized (assetDatabase) {
                    try {
                        assetDatabase.updateAssetName(assetID, name);
                        outputStream.writeObject(true);
                    } catch (Exception e) {
                        e.printStackTrace();
                        outputStream.writeObject(false);
                    }
                }
                outputStream.flush();
            }
            break;

            case DELETE_ASSET: {
                final Integer assetID = (Integer) inputStream.readObject();
                synchronized (assetDatabase) {
                    try {
                        assetDatabase.deleteAsset(assetID);
                        outputStream.writeObject(true);
                    } catch (Exception e) {
                        e.printStackTrace();
                        outputStream.writeObject(false);
                    }
                }
                outputStream.flush();
            }

            case GET_ASSET: {
                final Integer assetID = (Integer) inputStream.readObject();
                synchronized (assetDatabase) {
                    final Asset asset;
                    try {
                        asset = assetDatabase.getAsset(assetID);
                        outputStream.writeObject(true);
                        outputStream.writeObject(asset);

                    } catch (Exception e) {
                        outputStream.writeObject(false);
                        e.printStackTrace();
                    }

                }
                outputStream.flush();
            }
            break;

            case GET_ASSET_SET:{
                synchronized (assetDatabase) {
                    try {
                        Set<Asset> assetSet = assetDatabase.assetSet();
                        outputStream.writeObject(true);
                        outputStream.writeObject(assetSet);
                    } catch (Exception e) {
                        e.printStackTrace();
                        outputStream.writeObject(false);
                    }
                }
                outputStream.flush();
            }
            break;

            case CHECK_NAME:{
                final String name = (String) inputStream.readObject();
                synchronized (assetDatabase) {
                    final Boolean availability;
                    try {
                        availability = assetDatabase.checkName(name);
                        outputStream.writeObject(true);
                        outputStream.writeObject(availability);

                    } catch (Exception e) {
                        e.printStackTrace();
                        outputStream.writeObject(false);
                    }
                }
                outputStream.flush();
            }
            break;

            case ADD_ORG_ASSET: {
                final OrgAsset orgAsset = (OrgAsset) inputStream.readObject();
                synchronized (orgAssetDatabase) {
                    orgAssetDatabase.addAsset(orgAsset);
                }
            }
            break;

            case GET_ORG_ASSET: {
                final Integer orgID = (Integer) inputStream.readObject();
                final Integer assetID = (Integer) inputStream.readObject();

                synchronized (orgAssetDatabase) {
                    outputStream.writeObject(orgAssetDatabase.getOrgAsset(orgID, assetID));
                }
                outputStream.flush();
            }
            break;

            case GET_MY_ORG_ASSET_SET: {

                final Integer orgID = (Integer) inputStream.readObject();

                synchronized (orgAssetDatabase) {
                    outputStream.writeObject(orgAssetDatabase.myOrgAssetSet(orgID));
                }

                outputStream.flush();
            }
            break;

            case GET_ORG_ASSET_COUNT: {
                final Integer orgID = (Integer) inputStream.readObject();
                final Integer assetID = (Integer) inputStream.readObject();

                synchronized (orgAssetDatabase) {
                    outputStream.writeObject(orgAssetDatabase.checkAsset(orgID, assetID));
                }
                outputStream.flush();
            }
            break;

            case GET_ASSET_LIST: {
                final Integer orgID = (Integer) inputStream.readObject();

                synchronized (orgAssetDatabase) {
                    outputStream.writeObject(orgAssetDatabase.getAssetList(orgID));
                }
                outputStream.flush();
            }
            break;

            case UPDATE_ORG_ASSET_QUANTITY: {
                final Integer orgID = (Integer) inputStream.readObject();
                final Integer assetID = (Integer) inputStream.readObject();
                final Double quantity = (Double) inputStream.readObject();

                synchronized (orgAssetDatabase) {
                    orgAssetDatabase.updateQuantity(orgID, assetID, quantity);
                }
            }
            break;

            case LOGIN:{
                final String username = (String) inputStream.readObject();
                final String hashedPassword = (String) inputStream.readObject();

                synchronized (usersDatabase) {
                    try {
                        Boolean loginResult = usersDatabase.login(username, hashedPassword);
                        outputStream.writeObject(true); // Write true if add user is successful
                        outputStream.writeObject(loginResult);
                    } catch (Exception e) {
                        e.printStackTrace();
                        outputStream.writeObject(false);
                    }
                }
                outputStream.flush();
            }
            break;

            case ADD_BID:{
                final Bid bid = (Bid) inputStream.readObject();

                synchronized (bidDatabase) {
                    bidDatabase.addBid(bid);
                }
            }
            break;

            case GET_BID:{
                final Integer bidID = (Integer) inputStream.readObject();
                synchronized (bidDatabase) {
                    final Bid bid = bidDatabase.getBid(bidID);
                    outputStream.writeObject(bid);
                }
                outputStream.flush();
            }
            break;

            case CHANGE_PASSWORD: {

                final String newPassword = (String) inputStream.readObject();
                final Integer userID = (Integer) inputStream.readObject();

                synchronized (usersDatabase) {
                    try {
                        usersDatabase.changePassword(newPassword, userID);
                        outputStream.writeObject(true); // Write true if add user is successful

                    } catch (Exception e) {
                        e.printStackTrace();
                        outputStream.writeObject(false);
                    }
                }
                outputStream.flush();
            }
            break;

            case CHECK_PASSWORD: {
                final Integer userID = (Integer) inputStream.readObject();
                final String hashedPassword = (String) inputStream.readObject();

                synchronized (usersDatabase) {
                    try {
                        Boolean checkPassword = usersDatabase.checkPassword(userID, hashedPassword);
                        outputStream.writeObject(true);
                        outputStream.writeObject(checkPassword);
                    } catch (Exception e) {
                        e.printStackTrace();
                        outputStream.writeObject(false);
                    }
                }
                outputStream.flush();
            }
            break;

            case GET_BID_LIST: {
                final Integer orgID = (Integer) inputStream.readObject();
                final Boolean buyType = (Boolean) inputStream.readObject();

                synchronized (bidDatabase) {
                    outputStream.writeObject(bidDatabase.getBidList(orgID, buyType));
                }
                outputStream.flush();
            }
            break;

            case CHECK_TRADES: {
                synchronized (bidDatabase) {
                    bidDatabase.checkTrades();
                }
            }
            break;

            case UPDATE_ORG_UNIT: {
                final OrganisationUnit organisationUnit = (OrganisationUnit) inputStream.readObject();

                synchronized (organisationUnitDatabase) {
                    try {
                        organisationUnitDatabase.updateOrgUnit(organisationUnit);
                        outputStream.writeObject(true);
                    } catch (Exception e) {
                        e.printStackTrace();
                        outputStream.writeObject(false);
                    }
                }
                outputStream.flush();
            }
            break;

            case UPDATE_ORG_ASSET: {
                final OrgAsset orgAsset = (OrgAsset) inputStream.readObject();

                synchronized (orgAssetDatabase) {
                    orgAssetDatabase.updateOrgAsset(orgAsset);
                }
            }
            break;

            case GET_ORG_LIST: {
                synchronized (organisationUnitDatabase) {
                    try {
                        ArrayList<Object[]> objects = organisationUnitDatabase.getList();
                        outputStream.writeObject(true);
                        outputStream.writeObject(objects);

                    } catch (Exception e) {
                        e.printStackTrace();
                        outputStream.writeObject(false);
                    }
                }
                outputStream.flush();
            }
            break;

            case ADD_EMPLOYEE: {
                final UnitEmployee unitEmployee = (UnitEmployee) inputStream.readObject();

                synchronized (unitEmployeeDatabase) {
                    unitEmployeeDatabase.addEmployee(unitEmployee);
                }
            }
            break;

            case GET_EMPLOYEE: {
                final Integer userID = (Integer) inputStream.readObject();

                synchronized (unitEmployeeDatabase) {
                    outputStream.writeObject(unitEmployeeDatabase.getEmployee(userID));
                }
                outputStream.flush();
            }
            break;
        }
    }

    /**
     * Returns the port the server is configured to use
     *
     * @return The port number
     */
    public static int getPort() {
        return PORT;
    }

    /**
     * Starts the server running on the default port
     */
    public void start() throws IOException {
        // Connect to the database.
        // CREATE ALL DATABASE TABLES
        // Create all Data's to generate SQL Tables
        assetDatabase = new JDBCAssetDataSource();
        personsDatabase = new JDBCPersonsDataSource();
        usersDatabase = new JDBCUsersDataSource();
        organisationUnitDatabase = new JDBCOrganisationUnitDataSource();
        orgAssetDatabase = new JDBCOrgAssetDataSource();
        bidDatabase = new JDBCBidDataSource();
        unitEmployeeDatabase = new JDBCOrgUnitEmployeeDataSource();

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            serverSocket.setSoTimeout(SOCKET_ACCEPT_TIMEOUT);
            for (;;) {
                if (!running.get()) {
                    // The server is no longer running
                    break;
                }
                try {
                    final Socket socket = serverSocket.accept();
                    socket.setSoTimeout(SOCKET_READ_TIMEOUT);

                    // We have a new connection from a client. Use a runnable and thread to handle
                    // the client. The lambda here wraps the functional interface (Runnable).
                    final Thread clientThread = new Thread(() -> {
                            handleConnection(socket);
                    });
                    clientThread.start();
                } catch (SocketTimeoutException ignored) {
                    // Do nothing. A timeout is normal- we just want the socket to
                    // occasionally timeout so we can check if the server is still running
                } catch (Exception e) {
                    // We will report other exceptions by printing the stack trace, but we
                    // will not shut down the server. A exception can happen due to a
                    // client malfunction (or malicious client)
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            // If we get an error starting up, show an error dialog then exit
            e.printStackTrace();
            System.exit(1);
        }

        // Close down the server
        System.exit(0);
    }

    /**
     * Requests the server to shut down
     */
    public void shutdown() {
        // Shut the server down
        running.set(false);
    }
}
