package com.company.Testing.BidsTest;

import com.company.Common.DataSource.BidDataSource;
import com.company.Common.Model.Bid;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class MockBidDatabase implements BidDataSource {

    TreeMap<Integer, Bid> data;

    public MockBidDatabase() {
        data = new TreeMap<Integer, Bid>();
//        mockOrgUnitDatabase = new MockOrgUnitDatabase();

        DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        try {
            Timestamp sqlDate = new Timestamp(df.parse("04-06-2021 HH:MM:YY").getTime());
            Bid buyAssetBid = new Bid(1,1,1,"open",true, 4.00, 155.0, 20.0, sqlDate);
            Bid sellAssetBid = new Bid(2,1,1,"open", false, 3.50, 500.0, 302.0,sqlDate);

            data.put(1, buyAssetBid);
            data.put(2, sellAssetBid);

        } catch (ParseException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    /**
     * Add bid to database
     * @param bid
     * @throws Exception
     */
    @Override
    public void addBid(Bid bid) throws Exception {
        data.put(data.size()+1, bid);
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
        return data.get(bidID);
    }

    @Override
    public void close() {

    }

    @Override
    public ArrayList<Object[]> getBidList(Integer orgID, boolean buyType) throws Exception {
        return null;
    }

    @Override
    public ArrayList<Bid> getHistoryList(Integer assetID) throws Exception {
        return null;
    }

    @Override
    public void checkTrades() {
        // Find all sell bids
        Set<Bid> sellBids = new TreeSet<Bid>();
        for (Integer key: data.keySet()) {
            Bid bid = data.get(key);
            if (!bid.getBuyType()) {
                sellBids.add(bid);
            }
        }

        // Sort sell bids by date & price (processes by assetNumber, date, lowest selling price)

        // For each sell bid find all buy bids
        for (Bid sellBid: sellBids) {
            // Find all buy bids
            Set<Bid> buyBids = new TreeSet<Bid>();
            for (Integer key: data.keySet()) {
                Bid bid = data.get(key);

                if (bid.getBuyType() && bid.getAssetID() == sellBid.getAssetID()) buyBids.add(bid);
            }

            // Sort buy bids by price
            List<Bid> sortedList = new ArrayList<Bid>(buyBids);
            Collections.sort(sortedList);
            buyBids = new LinkedHashSet<>(sortedList);          //list -> set

            for (Bid buyBid: buyBids) {
                // Check that buy price is greater or equal to sell price
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

                    // Try Update Bids
                    try {
                        sellBid.addInactiveQuantity(purchasedAmount);
                        buyBid.addInactiveQuantity(purchasedAmount);

                        // Process Sale
                        // Remove purchase amount of units of asset from organisation unit
                        // Increase Org Unit Credits by (purchase amount x selling price)

                        // Process buy
                        // If asset does not exists create org asset for the number of units bought
                        // If asset does exist increase org unit asset by number of units bought

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    @Override
    public void updateBid(Integer bidID, Double activeQuantity, Double inactiveQuantity, Double purchaseAmount) throws Exception {

    }
}
