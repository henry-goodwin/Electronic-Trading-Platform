package com.company.Server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Date;

public class CheckTradesTimer {

    // Server details
    private static final String HOSTNAME = "127.0.0.1";
    private static final int PORT = 10000;

    private Socket socket;
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;

    /**
     * Setup Bid Network data source
     */
    public CheckTradesTimer() {
        try {
            // Persist a single connection through the whole lifetime of the application.
            // We will re-use this same connection/socket, rather than repeatedly opening
            // and closing connections.
            socket = new Socket(HOSTNAME, PORT);
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            outputStream.flush();
            inputStream = new ObjectInputStream(socket.getInputStream());
            checkTrades();
        } catch (IOException e) {
            // If the server connection fails, we're going to throw exceptions
            // whenever the application actually tries to query anything.
            // But it wasn't written to handle this, so make sure your
            // server is running beforehand!
            System.out.println("Failed to connect to server");
        }
    }

    private void checkTrades() {
        while (true) {
            System.out.println("Checked Trade");
            try {
                outputStream.writeObject(Command.CHECK_TRADES);
                outputStream.flush();
                Thread.sleep(10 * 1000);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
