package com.company.Common.NetworkDataSource;

import com.company.Common.DataSource.OrgUnitAssetDataSource;
import com.company.Common.Model.OrgAsset;
import com.company.Server.Command;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
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
    public void addAsset(OrgAsset orgAsset) throws Exception {
        outputStream.writeObject(Command.ADD_ORG_ASSET);
        outputStream.writeObject(orgAsset);
        outputStream.flush();
        if (!((Boolean) inputStream.readObject())) throw new Exception("Failed to add org asset, please try again");
    }

    @Override
    public OrgAsset getOrgAsset(Integer orgID, Integer assetID) throws Exception {
        outputStream.writeObject(Command.GET_ORG_ASSET);
        outputStream.writeObject(orgID);
        outputStream.writeObject(assetID);
        outputStream.flush();
        if (!((Boolean) inputStream.readObject())) throw new Exception("Failed to get org asset, please try again");
        OrgAsset orgAsset = (OrgAsset) inputStream.readObject();
        return orgAsset;
    }

    @Override
    public void close() {

    }

    @Override
    public Set<OrgAsset> OrgAssetSet() throws Exception {
        outputStream.writeObject(Command.GET_ORG_ASSET_SET);
        outputStream.flush();
        if (!((Boolean) inputStream.readObject())) throw new Exception("Failed to get org asset set, please try again");
        return (Set<OrgAsset>) inputStream.readObject();
    }

    @Override
    public Set<OrgAsset> myOrgAssetSet(Integer orgID) throws Exception {
        outputStream.writeObject(Command.GET_MY_ORG_ASSET_SET);
        outputStream.writeObject(orgID);
        outputStream.flush();
        if (!((Boolean) inputStream.readObject())) throw new Exception("Failed, please try again");
        return (Set<OrgAsset>) inputStream.readObject();
    }

    @Override
    public Boolean checkAsset(Integer orgID, Integer assetID) throws Exception {
        outputStream.writeObject(Command.GET_ORG_ASSET_COUNT);
        outputStream.writeObject(orgID);
        outputStream.writeObject(assetID);
        outputStream.flush();
        if (!((Boolean) inputStream.readObject())) throw new Exception("Failed to check org asset, please try again");
        return (Boolean) inputStream.readObject();
    }

    @Override
    public ArrayList<Object[]> getAssetList(Integer orgID) throws Exception {
        outputStream.writeObject(Command.GET_ASSET_LIST);
        outputStream.writeObject(orgID);
        outputStream.flush();
        if (!((Boolean) inputStream.readObject())) throw new Exception("Failed to get org asset list, please try again");
        return (ArrayList<Object[]>) inputStream.readObject();
    }

    @Override
    public void updateQuantity(Integer orgID, Integer assetID, Double quantity) throws Exception {
        outputStream.writeObject(Command.UPDATE_ORG_ASSET_QUANTITY);
        outputStream.writeObject(orgID);
        outputStream.writeObject(assetID);
        outputStream.writeObject(quantity);
        outputStream.flush();
        if (!((Boolean) inputStream.readObject())) throw new Exception("Failed to update org asset, please try again");
    }

    @Override
    public void updateOrgAsset(OrgAsset orgAsset) throws Exception {
            outputStream.writeObject(Command.UPDATE_ORG_ASSET);
            outputStream.writeObject(orgAsset);
            outputStream.flush();
            if (!((Boolean) inputStream.readObject())) throw new Exception("Failed to update org asset, please try again");
    }

    @Override
    public void updateData(Integer orgUnitID, Integer assetID, Double quantity) throws Exception {
        outputStream.writeObject(Command.UPDATE_DATA);
        outputStream.writeObject(orgUnitID);
        outputStream.writeObject(assetID);
        outputStream.writeObject(quantity);
        if (!((Boolean) inputStream.readObject())) throw new Exception("Failed to update org asset, please try again");
    }
}
