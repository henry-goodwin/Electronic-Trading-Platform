package com.company.Database.Bids;

import com.company.Database.DBConnector;
import com.company.Model.Bid;

import java.sql.*;

public class JDBCBidDataSource implements BidDataSource {

    private static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS `Bids` (" +
            "  `bidID` int(11) NOT NULL AUTO_INCREMENT," +
            "  `assetID` int(11) NOT NULL," +
            "  `organisationUnitID` int(11) NOT NULL," +
            "  `status` varchar(255) NOT NULL," +
            "  `buyType` boolean NOT NULL," +
            "  `price` double NOT NULL," +
            "  `quantity` double NOT NULL," +
            "  `date` date NOT NULL DEFAULT current_timestamp()," +
            "  PRIMARY KEY (`bidID`)," +
            "  FOREIGN KEY (`assetID`)" +
            "  REFERENCES Assets (`assetID`)" +
            "  ON DELETE CASCADE," +
            "  FOREIGN KEY (`organisationUnitID`)" +
            "  REFERENCES OrganisationUnit (`organisationUnitID`)" +
            "  ON DELETE CASCADE" +
            ");";

    private static final String INSERT_BID = "INSERT INTO `cab302`.`Bids`" +
            "(`assetID`," +
            "`organisationUnitID`," +
            "`status`," +
            "`buyType`," +
            "`price`," +
            "`quantity`)" +
            "VALUES" +
            "(?,?,?,?,?,?);";

    private static final String GET_BID = "SELECT *" +
            "FROM `cab302`.`Bids`" +
            "WHERE bidID=?;";

    private Connection connection;

    private PreparedStatement addBid;
    private PreparedStatement getBid;

    public JDBCBidDataSource() {
        connection = DBConnector.getInstance();

        try {
            Statement statement = connection.createStatement();
            statement.execute(CREATE_TABLE);

            addBid = connection.prepareStatement(INSERT_BID);
            getBid = connection.prepareStatement(GET_BID);

        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void addBid(Bid bid) {
        try {
            addBid.setInt(1, bid.getAssetID());
            addBid.setInt(2, bid.getOrgID());
            addBid.setString(3, bid.getStatus());
            addBid.setBoolean(4, bid.getBuyType());
            addBid.setDouble(5, bid.getPrice());
            addBid.setDouble(6, bid.getQuantity());
            addBid.execute();

        } catch (SQLException exception) {
            exception.printStackTrace();
        }


    }

    @Override
    public Bid getBid(Integer bidID) {
        Bid bid = new Bid();
        ResultSet resultSet = null;

        try {
            getBid.setInt(1, bidID);
            resultSet = getBid.executeQuery();

            if (resultSet.next()) {
                bid.setBidID(resultSet.getInt("bidID"));
                bid.setAssetID(resultSet.getInt("assetID"));
                bid.setOrgID(resultSet.getInt("organisationUnitID"));
                bid.setStatus(resultSet.getString("status"));
                bid.setBuyType(resultSet.getBoolean("buyType"));
                bid.setPrice(resultSet.getDouble("price"));
                bid.setQuantity(resultSet.getDouble("quantity"));
                bid.setDate(resultSet.getDate("date"));
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return bid;
    }

    @Override
    public void close() {
        try {
            connection.close();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }
}
