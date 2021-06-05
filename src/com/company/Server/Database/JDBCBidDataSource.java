package com.company.Server.Database;

import com.company.Common.DataSource.BidDataSource;
import com.company.Client.Data.AssetData;
import com.company.Client.Data.OrgAssetData;
import com.company.Client.Data.OrganisationUnitData;
import com.company.Common.Model.Asset;
import com.company.Common.Model.Bid;
import com.company.Common.Model.OrgAsset;
import com.company.Common.Model.OrganisationUnit;
import com.company.Common.NetworkDataSource.AssetNDS;
import com.company.Common.NetworkDataSource.OrgAssetNDS;
import com.company.Common.NetworkDataSource.OrganisationUnitNDS;

import java.sql.*;
import java.util.ArrayList;

public class JDBCBidDataSource implements BidDataSource {

    // SQL Statments
    private static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS `Bids` (" +
            "  `bidID` int(11) NOT NULL AUTO_INCREMENT," +
            "  `assetID` int(11) NOT NULL," +
            "  `organisationUnitID` int(11) NOT NULL," +
            "  `status` varchar(255) NOT NULL," +
            "  `buyType` boolean NOT NULL," +
            "  `price` double NOT NULL," +
            "  `activeQuantity` double NOT NULL," +
            "  `inactiveQuantity` double NOT NULL DEFAULT 0.0," +
            "  `date` DATETIME NOT NULL DEFAULT current_timestamp()," +
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
            "ORDER BY `assetID`, `date`, `price`;";

    private static final String GET_ACTIVE_BUY_LIST = "SELECT * " +
            "FROM `cab302`.`Bids`" +
            "WHERE (`buyType`, `status`, `assetID`) = (?,?,?)" +
            "ORDER BY (`date`);";

    private static final String UPDATE_BID = "UPDATE `cab302`.`Bids`" +
            "SET" +
            "`status` = ?," +
            "`inactiveQuantity` = ?" +
            "WHERE `bidID` = ?;";

    private static final String GET_HISTORY = "SELECT *" +
            "FROM `cab302`.`Bids`" +
            "WHERE (`buyType`, `status`, `assetID`) = (false, 'closed', ?);";

    // Database connection
    private Connection connection;

    // Prepared Statements
    private PreparedStatement addBid;
    private PreparedStatement getBid;
    private PreparedStatement getBidList;
    private PreparedStatement getActiveBids;
    private PreparedStatement getActiveBuys;
    private PreparedStatement updateBid;
    private PreparedStatement getHistory;

    /**
     * Constructor for Database
     */
    public JDBCBidDataSource() {
        // Connect to database
        connection = DBConnector.getInstance();

        try {
            // Try create table if it doesn't already exist
            Statement statement = connection.createStatement();
            statement.execute(CREATE_TABLE);

            // Prepare statements
            addBid = connection.prepareStatement(INSERT_BID);
            getBid = connection.prepareStatement(GET_BID);
            getBidList = connection.prepareStatement(GET_BID_LIST);
            getActiveBids = connection.prepareStatement(GET_ACTIVE_BID_LIST);
            getActiveBuys = connection.prepareStatement(GET_ACTIVE_BUY_LIST);
            updateBid = connection.prepareStatement(UPDATE_BID);
            getHistory = connection.prepareStatement(GET_HISTORY);

        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Add bid to the database
     * @param bid bid to set
     * @throws Exception Throws exception if bid fails
     */
    @Override
    public void addBid(Bid bid) throws Exception {
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
            throw new Exception("SQL Error");
        }
    }

    /**
     * Get bid from database
     * @param bidID bidID to search for
     * @return bid with bidID
     * @throws Exception Throws exception if no bid is found or if error is found
     */
    @Override
    public Bid getBid(Integer bidID) throws Exception {
        if (bidID < 0) throw new Exception("Bid ID is negative");
        ResultSet resultSet = null;

        try {
            Bid bid = new Bid();
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
                bid.setDate(resultSet.getTimestamp("date"));
                return bid;
            } else {
                throw new Exception("No Bid Found, please try again");
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
            throw new Exception("SQL Error");
        }
    }

    /**
     * Close connection to database
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
     * Get list of all active bids for orgID
     * @param orgID OrgID to search for
     * @param buyType type of bid (true = buy Bid, false = sell bid)
     * @return Array list of active bid
     * @throws Exception
     */
    @Override
    public ArrayList<Object[]> getBidList(Integer orgID, boolean buyType) throws Exception {
        ArrayList<Object[]> bidList = new ArrayList<>();
        ResultSet resultSet = null;

        AssetData assetData = null;
        try {
            assetData = new AssetData(new AssetNDS());
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
                    bid.setDate(resultSet.getTimestamp("date"));

                    Asset asset = assetData.get(resultSet.getInt("assetID"));

                    Object[] temp = new Object[] {bid ,asset, bid.getStatus(), bid.getPrice(), bid.getActiveQuantity(), bid.getInactiveQuantity(), bid.getDate()};
                    bidList.add(temp);
                }
                return bidList;

            } catch (SQLException exception) {
                exception.printStackTrace();
                throw new Exception("SQL Error");
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Error failed to get bidList");
        }
    }

    @Override
    public ArrayList<Bid> getHistoryList(Integer assetID) throws Exception {
        ArrayList<Bid> bidList = new ArrayList<>();
        ResultSet resultSet = null;

        getHistory.setInt(1, assetID);

        resultSet = getHistory.executeQuery();

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
            bid.setDate(resultSet.getTimestamp("date"));
            bidList.add(bid);
        }
        return bidList;
    }


    @Override
    public void checkTrades() throws Exception {
        // Get all active sell bids, sorted by assetID, lowest price & date
        ArrayList<Bid> sortedActiveBids = activeSellBids();

        // For each active sell bid
        for (Bid sellBid: sortedActiveBids) {
            // Find all buy bids, sorted by date (process earliest buy bids first)
            ArrayList<Bid> sortedActiveBuyBids = assetBuyBids(sellBid.getAssetID());

            // For each buy bid (for asset)
            for (Bid buyBid: sortedActiveBuyBids) {
                // Check that the buy price meets the sell price & that buy and sell bid org IDS are different
                if (buyBid.getPrice() >= sellBid.getPrice() && !buyBid.getOrgID().equals(sellBid.getOrgID())) {
                    // Check that bid is open
                    if (buyBid.getStatus().equals("open") && sellBid.getStatus().equals("open")) {

                        // Find out how much can be sold/bought
                        Double buyingQuantity = buyBid.getActiveQuantity() - buyBid.getInactiveQuantity();
                        Double sellingQuantity = sellBid.getActiveQuantity() - sellBid.getInactiveQuantity();

                        Double purchasedAmount;
                        if(buyingQuantity > sellingQuantity) {
                            purchasedAmount = sellingQuantity;
                        } else {
                            purchasedAmount = buyingQuantity;
                        }

                        // Purchase price
                        Double purchasePrice = purchasedAmount * sellBid.getPrice();

                        OrgAssetData orgAssetData = new OrgAssetData(new OrgAssetNDS());
                        OrganisationUnitData organisationUnitData = new OrganisationUnitData(new OrganisationUnitNDS());

                        // Check that organisation has enough credits to buy & organisation has enough asset to sell
                        if (checkAsset(orgAssetData, sellBid.getOrgID(), sellBid.getAssetID(), purchasedAmount) && checkCredits(organisationUnitData, buyBid.getOrgID(), purchasePrice)) {
                            // Update the bid in database
                            updateBid(buyBid.getBidID(), buyBid.getActiveQuantity(), buyBid.getInactiveQuantity(), purchasedAmount);
                            updateBid(sellBid.getBidID(), sellBid.getActiveQuantity(), sellBid.getInactiveQuantity(), purchasedAmount);

                            // Update bid in loop
                            buyBid.addInactiveQuantity(purchasedAmount);
                            sellBid.addInactiveQuantity(purchasedAmount);
                            if (sellBid.getActiveQuantity().equals(sellBid.getInactiveQuantity())) sellBid.setStatus("closed");
                            if (buyBid.getActiveQuantity().equals(buyBid.getInactiveQuantity())) sellBid.setStatus("closed");

                            // Process the sale of asset
                            updateOrgUnitCredits(organisationUnitData,sellBid.getOrgID(), purchasePrice);
                            updateOrgUnitAsset(orgAssetData,sellBid.getOrgID(), sellBid.getAssetID(), -purchasedAmount);

                            // Process the purchase of asset
                            updateOrgUnitCredits(organisationUnitData,buyBid.getOrgID(), -purchasePrice);
                            updateOrgUnitAsset(orgAssetData,buyBid.getOrgID(), buyBid.getAssetID(), purchasedAmount);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void updateBid(Integer bidID, Double activeQuantity, Double inactiveQuantity, Double purchaseAmount) throws Exception {
        Double newInactiveQuantity = inactiveQuantity + purchaseAmount;
        String newStatus = "open";
        if (activeQuantity.equals(newInactiveQuantity)) newStatus = "closed";

        updateBid.setString(1, newStatus);
        updateBid.setDouble(2, newInactiveQuantity);
        updateBid.setInt(3, bidID);

        updateBid.executeUpdate();
    }

    private void updateOrgUnitCredits(OrganisationUnitData organisationUnitData,Integer orgUnitID, Double creditsToUpdate) throws Exception {
        // Org Unit Database
        organisationUnitData.updateCredits(orgUnitID, creditsToUpdate);
    }

    private void updateOrgUnitAsset(OrgAssetData orgAssetData,Integer orgUnitID, Integer assetID, Double purchaseAmount) throws Exception{
        orgAssetData.updateData(orgUnitID, assetID, purchaseAmount);
    }

    private Boolean checkCredits(OrganisationUnitData organisationUnitData, Integer orgID, Double creditsToCheck) throws Exception {
        OrganisationUnit organisationUnit = organisationUnitData.get(orgID);
        return (organisationUnit.getCredits() >= creditsToCheck);
    }

    private Boolean checkAsset(OrgAssetData orgAssetData, Integer orgID, Integer assetID, Double quantityToCheck) throws Exception {
        OrgAsset orgAsset = orgAssetData.get(orgID, assetID);
        return (orgAsset.getQuantity() >= quantityToCheck);
    }


    /**
     * Gets array list of all active sell bids
     * @return array list of all active sell bids
     * @throws Exception Throw exception if error is found
     */
    private ArrayList<Bid> activeSellBids() throws Exception {
        ArrayList<Bid> activeSellBids = new ArrayList<Bid>();

        try {
            getActiveBids.setBoolean(1, false);
            getActiveBids.setString(2, "open");
            ResultSet sellResultSet = null;
            sellResultSet = getActiveBids.executeQuery();

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
                bid.setDate(sellResultSet.getTimestamp("date"));

                activeSellBids.add(bid);
            }

            return activeSellBids;

        } catch (SQLException exception) {
            exception.printStackTrace();
            throw new Exception("SQL Error");
        }
    }

    /**
     * Get array list of all active buy bids for a asset
     * @param assetID assetID ID to search for
     * @return array list of all active buy bids for an asset
     * @throws Exception Throw exception if error is found
     */
    private ArrayList<Bid> assetBuyBids(Integer assetID) throws Exception {

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
                buyBid.setDate(buyResultSet.getTimestamp("date"));
                assetBuyBids.add(buyBid);
            }

            return assetBuyBids;

        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw new Exception("SQL Error");
        }
    }
}
