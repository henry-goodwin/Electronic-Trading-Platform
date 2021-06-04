package com.company.NetworkDataSource;

import com.company.Database.Bids.BidDataSource;
import com.company.Model.Bid;
import com.company.Server.Command;
import com.company.Server.ServerException;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class BidNDS implements BidDataSource {

    // Server details
    private static final String HOSTNAME = "127.0.0.1";
    private static final int PORT = 10000;

    private Socket socket;
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;

    /**
     * Setup Bid Network data source
     */
    public BidNDS() {
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
     * Send bid to server so that it can be added to the database
     * @param bid Bid to add to database
     * @throws Exception Throw exception if fails
     */
    @Override
    public void addBid(Bid bid) throws Exception {
        try {
            outputStream.writeObject(Command.ADD_BID);
            outputStream.writeObject(bid);
            outputStream.flush();
            if (!((Boolean) inputStream.readObject())) throw new Exception("Failed to add bid, please try again");

        } catch (IOException e) {
            e.printStackTrace();
            throw new Exception("Failed to add bid, please try again");
        }
    }

    /**
     * Get Bid from server
     * @param bidID bidID to search for
     * @return Bid with the bidID
     * @throws Exception Throw exception if fails
     */
    @Override
    public Bid getBid(Integer bidID) throws Exception {
        try {
            outputStream.writeObject(Command.GET_BID);
            outputStream.writeObject(bidID);
            outputStream.flush();
            if (!((Boolean) inputStream.readObject())) throw new Exception("Failed to get bid, please try again");

            return ((Bid) inputStream.readObject());
        } catch (IOException e) {
            e.printStackTrace();
            throw new Exception("Failed to get bid, please try again");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new Exception("Failed to add get, please try again");
        }
    }

    @Override
    public void close() {

    }

    /**
     * Get Bid List from Server
     * @param orgID orgID to search for
     * @param buyType buyType to sell for (true = buy order, false = sell order)
     * @return Array list of object
     * @throws Exception Throw exception if fails
     */
    @Override
    public ArrayList<Object[]> getBidList(Integer orgID, boolean buyType) throws Exception {
        try {
            outputStream.writeObject(Command.GET_BID_LIST);
            outputStream.writeObject(orgID);
            outputStream.writeObject(buyType);
            outputStream.flush();
            if (!((Boolean) inputStream.readObject())) throw new Exception("Failed to get bid list, please try again");
            return (ArrayList<Object[]>) inputStream.readObject();

        } catch (IOException e) {
            e.printStackTrace();
            throw new Exception("Failed to get bid list, please try again");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new Exception("Failed to get bid list, please try again");
        }
    }

    /**
     * Check trades in database
     * @throws Exception
     */
    @Override
    public void checkTrades() throws Exception {
        try {
            outputStream.writeObject(Command.CHECK_TRADES);
            if (!((Boolean) inputStream.readObject())) throw new Exception("Failed to check trades, please try again");

        } catch (IOException e) {
            e.printStackTrace();
            throw new Exception("Failed to check trades, please try again");
        }
    }
}
