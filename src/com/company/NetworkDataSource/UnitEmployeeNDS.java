package com.company.NetworkDataSource;

import com.company.Database.OrgUnitEmployees.OrgUnitEmployeeDataSource;
import com.company.Model.UnitEmployee;
import com.company.Server.Command;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class UnitEmployeeNDS implements OrgUnitEmployeeDataSource {

    private static final String HOSTNAME = "127.0.0.1";
    private static final int PORT = 10000;

    private Socket socket;
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;


    public UnitEmployeeNDS() {
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
    public void addEmployee(UnitEmployee unitEmployee) {
        try {
            outputStream.writeObject(Command.ADD_EMPLOYEE);
            outputStream.writeObject(unitEmployee);
            outputStream.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public UnitEmployee getEmployee(Integer userID) {
        try {
            outputStream.writeObject(Command.GET_EMPLOYEE);
            outputStream.writeObject(userID);
            outputStream.flush();

            UnitEmployee unitEmployee = (UnitEmployee) inputStream.readObject();
            return unitEmployee;

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public void close() {

    }
}
