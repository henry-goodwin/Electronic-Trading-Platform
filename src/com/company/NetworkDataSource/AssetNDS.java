package com.company.NetworkDataSource;

import com.company.Database.Assets.AssetDataSource;
import com.company.Model.Asset;
import com.company.Model.OrganisationUnit;
import com.company.Utilities.Command;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

public class AssetNDS implements AssetDataSource {

    private static final String HOSTNAME = "127.0.0.1";
    private static final int PORT = 10000;

    private Socket socket;
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;


    public AssetNDS() {
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
    public void addAsset(Asset asset) {
        try {
            // tell the server to expect a person's details
            outputStream.writeObject(Command.ADD_ASSET);

            // send the actual data
            outputStream.writeObject(asset);
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Asset getAsset(Integer assetID) {
        try {
            outputStream.writeObject(Command.GET_ASSET);
            outputStream.writeObject(assetID);
            outputStream.flush();

            Asset asset = (Asset) inputStream.readObject();
            return asset;

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public Boolean checkName(String string) {
        try {
            outputStream.writeObject(Command.CHECK_NAME);
            outputStream.writeObject(string);
            outputStream.flush();

            return (Boolean) inputStream.readObject();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public void close() {

    }

    @Override
    public Set<Asset> assetSet() {
        try {
            outputStream.writeObject(Command.GET_ASSET_SET);
            outputStream.flush();
            Set<Asset> set = (Set<Asset>) inputStream.readObject();
            return set;
        } catch (IOException | ClassNotFoundException | ClassCastException e) {
            e.printStackTrace();
            return new HashSet<>();
        }
    }

    @Override
    public void updateAssetName(Integer assetID, String name) {
        try {
            outputStream.writeObject(Command.UPDATE_ASSET);
            outputStream.writeObject(assetID);
            outputStream.writeObject(name);
            outputStream.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteAsset(Integer assetID) {
        try {
            outputStream.writeObject(Command.DELETE_ASSET);
            outputStream.writeObject(assetID);
            outputStream.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
