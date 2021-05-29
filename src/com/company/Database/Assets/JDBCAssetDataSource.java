package com.company.Database.Assets;

import com.company.Database.DBConnector;
import com.company.Model.Asset;

import java.sql.*;
import java.util.Set;
import java.util.TreeSet;

public class JDBCAssetDataSource implements AssetDataSource {

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

    private Connection connection;

    private PreparedStatement addAsset;

    private PreparedStatement getAssetSet;

    private PreparedStatement getAsset;

    private PreparedStatement getCount;

    public JDBCAssetDataSource() {
        connection = DBConnector.getInstance();

        try {
            Statement statement = connection.createStatement();
            statement.execute(CREATE_TABLE);

            addAsset = connection.prepareStatement(INSERT_ASSET);
            getAsset = connection.prepareStatement(GET_ASSET);
            getAssetSet = connection.prepareStatement(GET_ASSET_SET);
            getCount = connection.prepareStatement(GET_COUNT);

        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void addAsset(Asset asset) {
        try {
            addAsset.setString(1, asset.getName());
            addAsset.execute();

        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public Asset getAsset(Integer assetID) {
        Asset asset = new Asset();
        ResultSet resultSet = null;

        try {
            getAsset.setInt(1, assetID);
            resultSet = getAsset.executeQuery();

            if (resultSet.next()) {

                asset.setAssetID(resultSet.getInt("assetID"));
                asset.setName(resultSet.getString("name"));

            }

        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        return asset;
    }

    @Override
    public Boolean checkName(String string) {
        Integer count = 0;
        ResultSet resultSet = null;

        try {
            getCount.setString(1, string);
            resultSet = getCount.executeQuery();

            if (resultSet.next()) {
                count = resultSet.getInt("COUNT(*)");
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return  (count <= 0);
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
    public Set<Asset> assetSet() {
        Set<Asset> assetSet = new TreeSet<Asset>();
        ResultSet resultSet = null;

        try {
            resultSet = getAssetSet.executeQuery();

            while (resultSet.next()) {
                Asset asset = new Asset(resultSet.getInt("assetID"), resultSet.getString("name"));
                assetSet.add(asset);
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        return assetSet;
    }
}
