package com.company.NetworkDataSource;

import com.company.Database.OrgUnitAssets.OrgUnitAssetDataSource;
import com.company.Model.OrgAsset;
import com.company.Model.OrganisationUnit;
import com.company.Utilities.Command;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class OrgAssetNDS implements OrgUnitAssetDataSource {

    private static final String HOSTNAME = "127.0.0.1";
    private static final int PORT = 10000;

    private Socket socket;
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;


    public OrgAssetNDS() {
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
    public void addAsset(OrgAsset orgAsset) {
        try {
            outputStream.writeObject(Command.ADD_ORG_ASSET);
            outputStream.writeObject(orgAsset);
            outputStream.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public OrgAsset getOrgAsset(Integer orgID, Integer assetID) {
        try {
            outputStream.writeObject(Command.GET_ORG_ASSET);
            outputStream.writeObject(orgID);
            outputStream.writeObject(assetID);
            outputStream.flush();

            OrgAsset orgAsset = (OrgAsset) inputStream.readObject();
            return orgAsset;

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return null;


    }

    @Override
    public void close() {

    }

    @Override
    public Set<OrgAsset> OrgAssetSet() {
        try {
            outputStream.writeObject(Command.GET_ORG_ASSET_SET);
            outputStream.flush();
            return (Set<OrgAsset>) inputStream.readObject();
        } catch (IOException | ClassNotFoundException | ClassCastException e) {
            e.printStackTrace();
            return new HashSet<>();
        }
    }

    @Override
    public Set<OrgAsset> myOrgAssetSet(Integer orgID) {
        try {
            outputStream.writeObject(Command.GET_MY_ORG_ASSET_SET);
            outputStream.writeObject(orgID);
            outputStream.flush();
            return (Set<OrgAsset>) inputStream.readObject();
        } catch (IOException | ClassNotFoundException | ClassCastException e) {
            e.printStackTrace();
            return new HashSet<>();
        }
    }

    @Override
    public Boolean checkAsset(Integer orgID, Integer assetID) {
        try {
            outputStream.writeObject(Command.GET_ORG_ASSET_COUNT);
            outputStream.writeObject(orgID);
            outputStream.writeObject(assetID);
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
    public ArrayList<Object[]> getAssetList(Integer orgID) {
        try {
            outputStream.writeObject(Command.GET_ASSET_LIST);
            outputStream.writeObject(orgID);
            outputStream.flush();
            return (ArrayList<Object[]>) inputStream.readObject();
        } catch (IOException | ClassNotFoundException | ClassCastException e) {
            e.printStackTrace();
            return new ArrayList<Object[]>();
        }
    }
}
