package com.company.Database.Assets;

import com.company.Database.DBConnector;
import com.company.Model.Asset;

import java.sql.*;
import java.util.Set;
import java.util.TreeSet;

public class JDBCAssetDataSource implements AssetDataSource {

    // SQL Statements
    private static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS `Assets` (" +
            "  `assetID` int(11) NOT NULL AUTO_INCREMENT," +
            "  `name` varchar(255) NOT NULL," +
            "  PRIMARY KEY (`assetID`)" +
            ");";

    private static final String INSERT_ASSET = "INSERT INTO `cab302`.`Assets`" +
            "(`name`)" +
            "VALUES" +
            "(?);";

    private static final String GET_ASSET_SET = "SELECT * FROM `cab302`.`Assets`;";

    private static final String GET_ASSET = "SELECT * FROM `cab302`.`Assets` WHERE assetID=?;";

    private static final String GET_COUNT = "SELECT COUNT(*) FROM `cab302`.`Assets` WHERE name=?;";

    private static final String UPDATE_NAME = "UPDATE `cab302`.`Assets`" +
            "SET" +
            "`name` = ?" +
            "WHERE `assetID` = ?;";

    private static final String DELETE_ASSET = "DELETE FROM `cab302`.`Assets`" +
            "WHERE assetID=?;";

    // Database Connection
    private Connection connection;

    // Prepared statements
    private PreparedStatement addAsset;
    private PreparedStatement getAssetSet;
    private PreparedStatement getAsset;
    private PreparedStatement getCount;
    private PreparedStatement updateName;
    private PreparedStatement deleteAsset;

    /**
     * Constructor
     */
    public JDBCAssetDataSource() {

        // Connect to database
        connection = DBConnector.getInstance();

        try {
            // Try create the table if does not exist
            Statement statement = connection.createStatement();
            statement.execute(CREATE_TABLE);

            // Prepare statements
            addAsset = connection.prepareStatement(INSERT_ASSET);
            getAsset = connection.prepareStatement(GET_ASSET);
            getAssetSet = connection.prepareStatement(GET_ASSET_SET);
            getCount = connection.prepareStatement(GET_COUNT);
            updateName = connection.prepareStatement(UPDATE_NAME);
            deleteAsset = connection.prepareStatement(DELETE_ASSET);

        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Add asset to the database
     * @param asset Asset to add
     * @throws Exception Throw exception if fails
     */
    @Override
    public void addAsset(Asset asset) throws Exception {
        if (asset.getName().equals("")) throw new Exception("Empty Asset Name");
        try {
            addAsset.setString(1, asset.getName());
            addAsset.execute();

        } catch (SQLException exception) {
            exception.printStackTrace();
            throw new Exception("SQL Error");
        }
    }

    /**
     * Get Asset from the database
     * @param assetID The assetID as a Integer to search for.
     * @return return asset with the ID
     * @throws Exception throws exception if getAsset fails
     */
    @Override
    public Asset getAsset(Integer assetID) throws Exception {
        if (assetID < 0) throw new Exception();
        ResultSet resultSet = null;

        try {
            getAsset.setInt(1, assetID);
            resultSet = getAsset.executeQuery();

            if (resultSet.next()) {
                Asset asset = new Asset();
                asset.setAssetID(resultSet.getInt("assetID"));
                asset.setName(resultSet.getString("name"));
                return asset;
            } else {
                throw new Exception("No asset found");
            }

        } catch (SQLException exception) {
            exception.printStackTrace();
            throw new Exception("SQL Error");
        }
    }

    /**
     * Check that asset name exists in database, used to ensure duplicates are not entered
     * @param string name to search for
     * @return Return true if name is free, return false if name is taken
     * @throws Exception throw exception if fails
     */
    @Override
    public Boolean checkName(String string) throws Exception {

        if (string.equals("")) throw new Exception("Error: Empty Name");

        ResultSet resultSet = null;

        try {
            Integer count = 0;
            getCount.setString(1, string);
            resultSet = getCount.executeQuery();

            if (resultSet.next()) {
                count = resultSet.getInt("COUNT(*)");
                return  (count <= 0);
            }

            return  (count <= 0);

        } catch (SQLException exception) {
            exception.printStackTrace();
            throw new Exception("Sql error");
        }
    }

    /**
     * Close connection to the database
     */
    @Override
    public void close() {
        try {
            connection.close();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Get set of all assets
     * @return asset set
     * @throws Exception throw exception if fails
     */
    @Override
    public Set<Asset> assetSet() throws Exception {
        ResultSet resultSet = null;

        try {
            Set<Asset> assetSet = new TreeSet<Asset>();
            resultSet = getAssetSet.executeQuery();

            while (resultSet.next()) {
                Asset asset = new Asset(resultSet.getInt("assetID"), resultSet.getString("name"));
                assetSet.add(asset);
            }

            return assetSet;


        } catch (SQLException exception) {
            exception.printStackTrace();
            throw new Exception("SQL Error");
        }
    }

    /**
     * Update the name of asset in database
     * @param assetID assetID to search for
     * @param name name to update
     * @throws Exception Throws exception if fails
     */
    @Override
    public void updateAssetName(Integer assetID, String name) throws Exception {
        if (assetID < 0) throw new Exception("Negative Asset ID");
        if (name.equals("")) throw new Exception("Error: Empty name");
        try {
            updateName.setString(1, name);
            updateName.setInt(2, assetID);
            updateName.executeUpdate();

        } catch (SQLException exception) {
            exception.printStackTrace();
            throw new Exception("SQL Error");
        }
    }

    /**
     * Delete asset with the ID
     * @param assetID assetID to delete
     * @throws Exception throw exception if fails
     */
    @Override
    public void deleteAsset(Integer assetID) throws Exception {
        if (assetID < 0) throw new Exception("Error: Negative assetID");
        try {
            deleteAsset.setInt(1, assetID);
            deleteAsset.executeUpdate();

        } catch (SQLException exception) {
            exception.printStackTrace();
            throw new Exception("SQL Error");
        }

    }
}
