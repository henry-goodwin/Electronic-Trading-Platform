package com.company.Database.Bids;

import com.company.Database.Assets.AssetData;
import com.company.Database.DBConnector;
import com.company.Database.OrgUnitAssets.OrgAssetData;
import com.company.Database.OrganisationUnit.OrganisationUnitData;
import com.company.Model.Asset;
import com.company.Model.Bid;
import com.company.Model.OrgAsset;
import com.company.Model.OrganisationUnit;
import com.company.NetworkDataSource.AssetNDS;
import com.company.NetworkDataSource.BidNDS;
import com.company.NetworkDataSource.OrgAssetNDS;
import com.company.NetworkDataSource.OrganisationUnitNDS;

import java.sql.*;
import java.util.ArrayList;

public class JDBCBidDataSource implements BidDataSource {

    private static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS `Bids` (" +
            "  `bidID` int(11) NOT NULL AUTO_INCREMENT," +
            "  `assetID` int(11) NOT NULL," +
            "  `organisationUnitID` int(11) NOT NULL," +
            "  `status` varchar(255) NOT NULL," +
            "  `buyType` boolean NOT NULL," +
            "  `price` double NOT NULL," +
            "  `activeQuantity` double NOT NULL," +
            "  `inactiveQuantity` double NOT NULL DEFAULT 0.0," +
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
            "`activeQuantity`)" +
            "VALUES" +
            "(?,?,?,?,?,?);";

    private static final String GET_BID = "SELECT *" +
            "FROM `cab302`.`Bids`" +
            "WHERE bidID=?;";

    private static final String GET_BID_LIST = "SELECT * " +
            "FROM `cab302`.`Bids`" +
            "WHERE (`organisationUnitID`, `buyType`, `status`) = (?,?,?);";

    private static final String GET_ACTIVE_BID_LIST = "SELECT * " +
            "FROM `cab302`.`Bids`" +
            "WHERE (`buyType`, `status`) = (?,?)" +
            "ORDER BY `date`;";

    private static final String GET_ACTIVE_BUY_LIST = "SELECT * " +
            "FROM `cab302`.`Bids`" +
            "WHERE (`buyType`, `status`, `assetID`) = (?,?,?)" +
            "ORDER BY (`price`);";

    private static final String UPDATE_BID = "UPDATE `cab302`.`Bids`" +
            "SET" +
            "`status` = ?," +
            "`inactiveQuantity` = ?" +
            "WHERE `bidID` = ?;";

    private Connection connection;

    private PreparedStatement addBid;
    private PreparedStatement getBid;
    private PreparedStatement getBidList;
    private PreparedStatement getActiveBids;
    private PreparedStatement getActiveBuys;
    private PreparedStatement updateBid;

    public JDBCBidDataSource() {
        connection = DBConnector.getInstance();

        try {
            Statement statement = connection.createStatement();
            statement.execute(CREATE_TABLE);

            addBid = connection.prepareStatement(INSERT_BID);
            getBid = connection.prepareStatement(GET_BID);
            getBidList = connection.prepareStatement(GET_BID_LIST);
            getActiveBids = connection.prepareStatement(GET_ACTIVE_BID_LIST);
            getActiveBuys = connection.prepareStatement(GET_ACTIVE_BUY_LIST);
            updateBid = connection.prepareStatement(UPDATE_BID);

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
            addBid.setDouble(6, bid.getActiveQuantity());
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
                bid.setActiveQuantity(resultSet.getDouble("activeQuantity"));
                bid.setInactiveQuantity(resultSet.getDouble("inactiveQuantity"));
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

    @Override
    public ArrayList<Object[]> getBidList(Integer orgID, boolean buyType) {
        ArrayList<Object[]> assetList = new ArrayList<>();
        ResultSet resultSet = null;

        AssetData assetData = new AssetData(new AssetNDS());

        try {
            getBidList.setInt(1, orgID);
            getBidList.setBoolean(2, buyType);
            getBidList.setString(3, "open");

            resultSet = getBidList.executeQuery();

            while (resultSet.next()) {
                // Find Asset
                Bid bid = new Bid();
                bid.setBidID(resultSet.getInt("bidID"));
                bid.setAssetID(resultSet.getInt("assetID"));
                bid.setOrgID(resultSet.getInt("organisationUnitID"));
                bid.setStatus(resultSet.getString("status"));
                bid.setBuyType(resultSet.getBoolean("buyType"));
                bid.setPrice(resultSet.getDouble("price"));
                bid.setActiveQuantity(resultSet.getDouble("activeQuantity"));
                bid.setInactiveQuantity(resultSet.getDouble("inactiveQuantity"));
                bid.setDate(resultSet.getDate("date"));

                Asset asset = assetData.get(resultSet.getInt("assetID"));

                Object[] temp = new Object[] {bid ,asset, bid.getStatus(), bid.getPrice(), bid.getActiveQuantity(), bid.getInactiveQuantity(), bid.getDate()};
                assetList.add(temp);
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return assetList;
    }

    @Override
    public void checkTrades() {
        // Get all active sell bids
        ArrayList<Bid> activeSellBids = activeSellBids();
        OrganisationUnitData organisationUnitData = new OrganisationUnitData(new OrganisationUnitNDS());
        OrgAssetData orgAssetData = new OrgAssetData(new OrgAssetNDS());

        // For each sell bid in active sell bids
        for (Bid sellBid: activeSellBids) {
            ArrayList<Bid> assetBuyBids = assetBuyBids(sellBid.getAssetID());

            for (Bid buyBid: assetBuyBids) {
                // Check that buy price meets sell price
                if (buyBid.getPrice() >= sellBid.getPrice()) {
                    // Find how much is available to buy and sell
                    Double buyingQuantity = buyBid.getActiveQuantity() - buyBid.getInactiveQuantity();
                    Double sellingQuantity = sellBid.getActiveQuantity() - sellBid.getInactiveQuantity();

                    Double purchasedAmount;
                    if(buyingQuantity > sellingQuantity) {
                        purchasedAmount = sellingQuantity;
                    } else {
                        purchasedAmount = buyingQuantity;
                    }

                    // Update Bids
                    sellBid.updateInactiveQuantity(purchasedAmount);
                    buyBid.updateInactiveQuantity(purchasedAmount);

                    // Mark as closed if all units are bought
                    if (buyBid.getActiveQuantity().equals(buyBid.getInactiveQuantity())) {
                        // Mark as sold
                        buyBid.setStatus("closed");
                    }

                    if (sellBid.getActiveQuantity().equals(sellBid.getInactiveQuantity())) {
                        // Mark as sold
                        sellBid.setStatus("closed");
                    }

                    // Update org units credits
                    // Get Buy Org Unit
                    OrganisationUnit buyOrganisationUnit = organisationUnitData.get(buyBid.getOrgID());

                    try {
                        buyOrganisationUnit.removeCredits(purchasedAmount * buyBid.getPrice());
                    } catch (Exception e) {
                        e.printStackTrace();
                        break;
                    }

                    // Get Sell Org Unit
                    OrganisationUnit sellOrganisationUnit = organisationUnitData.get(sellBid.getOrgID());
                    try {
                        sellOrganisationUnit.addCredits(purchasedAmount * sellBid.getPrice());
                    } catch (Exception e) {
                        e.printStackTrace();
                        break;
                    }

                    OrgAsset buyOrgAsset = orgAssetData.get(buyBid.getOrgID(), buyBid.getAssetID());
                    buyOrgAsset.addQuantity(purchasedAmount);

                    OrgAsset sellOrgAsset = orgAssetData.get(sellBid.getOrgID(), sellBid.getAssetID());
                    sellOrgAsset.removeQuantity(purchasedAmount);

                    syncData(organisationUnitData, orgAssetData, buyBid, sellBid, buyOrganisationUnit, sellOrganisationUnit, buyOrgAsset, sellOrgAsset);
                }
            }

        }
    }

    private ArrayList<Bid> activeSellBids() {
        ArrayList<Bid> activeSellBids = new ArrayList<Bid>();

        try {
            getActiveBids.setBoolean(1, false);
            getActiveBids.setBoolean(2, false);
            ResultSet sellResultSet = null;
            sellResultSet = getBidList.executeQuery();

            while (sellResultSet.next()) {
                Bid bid = new Bid();
                bid.setBidID(sellResultSet.getInt("bidID"));
                bid.setAssetID(sellResultSet.getInt("assetID"));
                bid.setOrgID(sellResultSet.getInt("organisationUnitID"));
                bid.setStatus(sellResultSet.getString("status"));
                bid.setBuyType(sellResultSet.getBoolean("buyType"));
                bid.setPrice(sellResultSet.getDouble("price"));
                bid.setActiveQuantity(sellResultSet.getDouble("activeQuantity"));
                bid.setInactiveQuantity(sellResultSet.getDouble("inactiveQuantity"));
                bid.setDate(sellResultSet.getDate("date"));

                activeSellBids.add(bid);
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return activeSellBids;
    }

    private ArrayList<Bid> assetBuyBids(Integer assetID) {

        ArrayList<Bid> assetBuyBids = new ArrayList<Bid>();

        try {
            getActiveBuys.setBoolean(1, true);
            getActiveBuys.setString(2, "open");
            getActiveBuys.setInt(3, assetID);

            ResultSet buyResultSet = null;
            buyResultSet = getActiveBuys.executeQuery();

            while (buyResultSet.next()) {
                Bid buyBid = new Bid();
                buyBid.setBidID(buyResultSet.getInt("bidID"));
                buyBid.setAssetID(buyResultSet.getInt("assetID"));
                buyBid.setOrgID(buyResultSet.getInt("organisationUnitID"));
                buyBid.setStatus(buyResultSet.getString("status"));
                buyBid.setBuyType(buyResultSet.getBoolean("buyType"));
                buyBid.setPrice(buyResultSet.getDouble("price"));
                buyBid.setActiveQuantity(buyResultSet.getDouble("activeQuantity"));
                buyBid.setInactiveQuantity(buyResultSet.getDouble("inactiveQuantity"));
                buyBid.setDate(buyResultSet.getDate("date"));
                assetBuyBids.add(buyBid);
            }
        } catch (SQLException throwables) {
                throwables.printStackTrace();
        }
        return assetBuyBids;
    }

    private void syncData(OrganisationUnitData organisationUnitData, OrgAssetData orgAssetData, Bid buyBid, Bid sellBid, OrganisationUnit buyUnit, OrganisationUnit sellUnit, OrgAsset buyOrgAsset, OrgAsset sellOrgAsset) {
        try {

//         Update Buy Bid in database
            updateBid.setString(1, buyBid.getStatus());
            updateBid.setDouble(2, buyBid.getInactiveQuantity());
            updateBid.setInt(3, buyBid.getBidID());
            updateBid.executeUpdate();

            // Update Sell Bid in database
            updateBid.setString(1, sellBid.getStatus());
            updateBid.setDouble(2, sellBid.getInactiveQuantity());
            updateBid.setInt(3, sellBid.getBidID());
            updateBid.executeUpdate();

            organisationUnitData.updateUnit(buyUnit);
            organisationUnitData.updateUnit(sellUnit);

            orgAssetData.updateOrgAsset(buyOrgAsset);
            orgAssetData.updateOrgAsset(sellOrgAsset);

        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

}
