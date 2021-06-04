package com.company.NetworkDataSource;

import com.company.Database.Assets.AssetDataSource;
import com.company.Model.Asset;
import com.company.Server.Command;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

public class AssetNDS implements AssetDataSource {

    // Database configuration
    private static final String HOSTNAME = "127.0.0.1";
    private static final int PORT = 10000;

    private Socket socket;
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;


    /**
     * Construct network data source
     */
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

    /**
     * Sends asset to the server so that it can be added to the database
     * @param asset Asset to add
     * @throws Exception Throws exception if fails
     */
    @Override
    public void addAsset(Asset asset) throws Exception {
        try {
            // tell the server to expect a person's details
            outputStream.writeObject(Command.ADD_ASSET);

            // send the actual data
            outputStream.writeObject(asset);
            outputStream.flush();
            if (!((Boolean) inputStream.readObject())) throw new Exception("Failed to add asset, please try again");

        } catch (IOException e) {
            e.printStackTrace();
            throw new Exception("Failed to add asset, please try again");
        }
    }

    /**
     * Gets asset from the server
     * @param assetID The assetID as a Integer to search for.
     * @return Asset with the assetID
     * @throws Exception throws exception if fails
     */
    @Override
    public Asset getAsset(Integer assetID) throws Exception {
        try {
            outputStream.writeObject(Command.GET_ASSET);
            outputStream.writeObject(assetID);
            outputStream.flush();

            if (!((Boolean) inputStream.readObject())) throw new Exception("Failed to get asset, please try again");

            Asset asset = (Asset) inputStream.readObject();
            return asset;

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            throw new Exception("Failed to get person, please try again");
        }
    }

    /**
     * Sends name to server to see if it exists in the database
     * @param string asset name to search for
     * @return Return true if asset exists, return false if asset does not exist
     * @throws Exception Throw exception if fails
     */
    @Override
    public Boolean checkName(String string) throws Exception {
        try {
            outputStream.writeObject(Command.CHECK_NAME);
            outputStream.writeObject(string);
            outputStream.flush();
            if (!((Boolean) inputStream.readObject())) throw new Exception("Failed to check name");
            return (Boolean) inputStream.readObject();

        } catch (IOException e) {
            e.printStackTrace();
            throw new Exception("Failed to check name");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new Exception("Failed to check name");
        }
    }

    @Override
    public void close() {

    }

    /**
     * Get asset set from the server
     * @return Set of all assets in database
     * @throws Exception
     */
    @Override
    public Set<Asset> assetSet() throws Exception {
        try {
            outputStream.writeObject(Command.GET_ASSET_SET);
            outputStream.flush();
            if (!((Boolean) inputStream.readObject())) throw new Exception("Failed to get set, please try again");
            Set<Asset> set = (Set<Asset>) inputStream.readObject();
            return set;
        } catch (IOException | ClassNotFoundException | ClassCastException e) {
            e.printStackTrace();
            throw new Exception("Failed to get set, please try again");
        }
    }

    /**
     * Sends new assetName to server to update asset name in database
     * @param assetID assetID to update
     * @param name name to update
     * @throws Exception
     */
    @Override
    public void updateAssetName(Integer assetID, String name) throws Exception {
        try {
            outputStream.writeObject(Command.UPDATE_ASSET);
            outputStream.writeObject(assetID);
            outputStream.writeObject(name);
            outputStream.flush();
            if (!((Boolean) inputStream.readObject())) throw new Exception("Failed to update asset, please try again");
        } catch (IOException e) {
            e.printStackTrace();
            throw new Exception("Failed to update asset, please try again");
        }
    }

    /**
     * Send assetID to the server so that it can be deleted from the database
     * @param assetID AssetID to delete
     * @throws Exception
     */
    @Override
    public void deleteAsset(Integer assetID) throws Exception {
        try {
            outputStream.writeObject(Command.DELETE_ASSET);
            outputStream.writeObject(assetID);
            outputStream.flush();
            if (!((Boolean) inputStream.readObject())) throw new Exception("Failed to delete asset, please try again");

        } catch (IOException e) {
            e.printStackTrace();
            throw new Exception("Failed to delete asset, please try again");

        }
    }
}
