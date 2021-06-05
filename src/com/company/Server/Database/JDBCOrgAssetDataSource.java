package com.company.Server.Database;

import com.company.Common.DataSource.OrgUnitAssetDataSource;
import com.company.Common.Model.OrgAsset;

import java.sql.*;
import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;

public class JDBCOrgAssetDataSource implements OrgUnitAssetDataSource {

    // SQL Statements
    private static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS `OrganisationUnitAssets` (" +
            "`organisationUnitID` int(11) NOT NULL," +
            "`assetID` int(11) NOT NULL," +
            "`quantity` double DEFAULT NULL," +
            "PRIMARY KEY (`organisationUnitID`,`assetID`)," +
            "FOREIGN KEY (`assetID`)" +
            "REFERENCES Assets (`assetID`)" +
            "ON DELETE CASCADE," +
            "FOREIGN KEY (`organisationUnitID`)" +
            "REFERENCES OrganisationUnit (`organisationUnitID`)" +
            "ON DELETE CASCADE" +
            ");";

    private static final String INSERT_ORG_ASSET = "INSERT INTO `cab302`.`OrganisationUnitAssets`" +
            "(`organisationUnitID`," +
            "`assetID`," +
            "`quantity`)" +
            "VALUES" +
            "(?,?,?);";

    private static final String GET_ORG_ASSET_SET = "SELECT * FROM `cab302`.`OrganisationUnitAssets`;";

    private static final String GET_MY_ORG_ASSET_SET = "SELECT * FROM `cab302`.`OrganisationUnitAssets` WHERE (organisationUnitID) = (?);";

    private static final String GET_ORG_ASSET = "SELECT *" +
            "FROM `cab302`.`OrganisationUnitAssets`" +
            "WHERE (organisationUnitID,assetID) = (?,?);";

    private static final String GET_ASSET_COUNT = "SELECT COUNT(*)" +
            "FROM `cab302`.`OrganisationUnitAssets`" +
            "WHERE (organisationUnitID,assetID) = (?,?);";

    private static final String UPDATE_ASSET_QUANTITY = "UPDATE `cab302`.`OrganisationUnitAssets`" +
            "SET" +
            "`quantity` = ?" +
            "WHERE `organisationUnitID` = ? AND `assetID` = ?;";

    private static final String UPDATE_ASSET = "UPDATE `cab302`.`OrganisationUnitAssets`" +
            "SET `quantity` = ?" +
            "WHERE `organisationUnitID` = ? AND `assetID` = ?;";

    private Connection connection;

    private PreparedStatement addOrgAsset;
    private PreparedStatement getAssetSet;
    private PreparedStatement getOrgAsset;
    private PreparedStatement getMyAssetSet;
    private PreparedStatement getAssetCount;
    private PreparedStatement updateAssetQuantity;
    private PreparedStatement updateAsset;

    public JDBCOrgAssetDataSource() {
        connection = DBConnector.getInstance();

        try {
            Statement statement = connection.createStatement();
            statement.execute(CREATE_TABLE);

            addOrgAsset = connection.prepareStatement(INSERT_ORG_ASSET);
            getOrgAsset = connection.prepareStatement(GET_ORG_ASSET);
            getAssetSet = connection.prepareStatement(GET_ORG_ASSET_SET);
            getMyAssetSet = connection.prepareStatement(GET_MY_ORG_ASSET_SET);
            getAssetCount = connection.prepareStatement(GET_ASSET_COUNT);
            updateAssetQuantity = connection.prepareStatement(UPDATE_ASSET_QUANTITY);
            updateAsset = connection.prepareStatement(UPDATE_ASSET);

        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }


    @Override
    public void addAsset(OrgAsset orgAsset) throws Exception {
        try {
            addOrgAsset.setInt(1, orgAsset.getOrganisationUnitID());
            addOrgAsset.setInt(2, orgAsset.getAssetID());
            addOrgAsset.setDouble(3, orgAsset.getQuantity());
            addOrgAsset.execute();

        } catch (SQLException exception) {
            exception.printStackTrace();
            throw new Exception("SQL Error");
        }
    }

    @Override
    public OrgAsset getOrgAsset(Integer orgID, Integer assetID) throws Exception {
        ResultSet resultSet = null;

        try {
            getOrgAsset.setInt(1, orgID);
            getOrgAsset.setInt(2, assetID);
            resultSet = getOrgAsset.executeQuery();

            if (resultSet.next()) {

                return new OrgAsset(resultSet.getInt("organisationUnitID"), resultSet.getInt("assetID"), resultSet.getDouble("quantity"));

            } else {
                throw new Exception("Error: no org asset found");
            }

        } catch (SQLException exception) {
            exception.printStackTrace();
            throw new Exception("SQL error");
        }
    }

    @Override
    public void close() {
        try {
            connection.close();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public Set<OrgAsset> OrgAssetSet() throws Exception {
        Set<OrgAsset> orgAssetSet = new TreeSet<OrgAsset>();
        ResultSet resultSet = null;

        try {
            resultSet = getAssetSet.executeQuery();

            while (resultSet.next()) {
                OrgAsset orgAsset = new OrgAsset(resultSet.getInt("organisationUnitID"), resultSet.getInt("assetID"), resultSet.getDouble("quantity"));
                orgAssetSet.add(orgAsset);
            }
            return orgAssetSet;
        } catch (SQLException exception) {
            exception.printStackTrace();
            throw exception;
        }
    }

    @Override
    public Set<OrgAsset> myOrgAssetSet(Integer orgID) throws Exception {
        Set<OrgAsset> orgAssetSet = new TreeSet<OrgAsset>();
        ResultSet resultSet = null;

        try {
            getMyAssetSet.setInt(1, orgID);
            resultSet = getMyAssetSet.executeQuery();

            while (resultSet.next()) {
                // Check that asset exists
                OrgAsset orgAsset = new OrgAsset(resultSet.getInt("organisationUnitID"), resultSet.getInt("assetID"), resultSet.getDouble("quantity"));
                orgAssetSet.add(orgAsset);
            }
            return orgAssetSet;

        } catch (SQLException exception) {
            exception.printStackTrace();
            throw exception;
        }
    }

    @Override
    public Boolean checkAsset(Integer orgID, Integer assetID) throws Exception {
        Integer count = 0;
        ResultSet resultSet = null;

        try {
            getAssetCount.setInt(1, orgID);
            getAssetCount.setInt(2, assetID);
            resultSet = getAssetCount.executeQuery();

            if (resultSet.next()) {
                count = resultSet.getInt("COUNT(*)");
            }

            return  (count <= 0);

        } catch (SQLException exception) {
            throw exception;
        }
    }

    @Override
    public ArrayList<Object[]> getAssetList(Integer orgID) throws Exception {
        ArrayList<Object[]> assetList = new ArrayList<>();
        ResultSet resultSet = null;

        try {
            getMyAssetSet.setInt(1, orgID);
            resultSet = getMyAssetSet.executeQuery();

            while (resultSet.next()) {
                // Find Asset
                OrgAsset asset = getOrgAsset(orgID, resultSet.getInt("assetID"));
                Object[] temp = new Object[] {asset, resultSet.getDouble("quantity")};
                assetList.add(temp);
            }

            return assetList;

        } catch (SQLException exception) {
            throw exception;
        }
    }

    @Override
    public void updateQuantity(Integer orgID, Integer assetID, Double quantity) throws Exception {
        try {
            updateAssetQuantity.setDouble(1, quantity);
            updateAssetQuantity.setInt(2, orgID);
            updateAssetQuantity.setInt(3, assetID);
            updateAssetQuantity.executeUpdate();

        } catch (SQLException exception) {
            throw exception;
        }
    }

    @Override
    public void updateOrgAsset(OrgAsset orgAsset) throws Exception {
        try {
            updateAsset.setDouble(1, orgAsset.getQuantity());
            updateAsset.setInt(2, orgAsset.getOrganisationUnitID());
            updateAsset.setInt(3, orgAsset.getAssetID());
            updateAsset.executeUpdate();

        } catch (SQLException exception) {
            throw exception;
        }
    }

    @Override
    public void updateData(Integer orgUnitID, Integer assetID, Double quantity) throws Exception {
        // Check if asset exists in org
        if (!checkAsset(orgUnitID, assetID)) {
            // Asset Exists, update asset
            OrgAsset existingOrgAsset = getOrgAsset(orgUnitID, assetID);
            Double newQuantity = existingOrgAsset.getQuantity() + quantity;
            updateAsset.setDouble(1, newQuantity);
            updateAsset.setInt(2, orgUnitID);
            updateAsset.setInt(3, assetID);
            updateAsset.executeUpdate();

        } else {
            // Asset Does not exist, insert new asset
            OrgAsset newOrgAsset = new OrgAsset(orgUnitID, assetID, quantity);
            addAsset(newOrgAsset);
        }
    }
}
