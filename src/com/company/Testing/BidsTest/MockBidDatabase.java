package com.company.Testing.BidsTest;

import com.company.Database.Bids.BidDataSource;
import com.company.Model.Bid;

import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.TreeMap;

public class MockBidDatabase implements BidDataSource {

    TreeMap<Integer, Bid> data;

    public MockBidDatabase() {
        data = new TreeMap<Integer, Bid>();

        DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        try {
            Date sqlDate = new Date(df.parse("04-06-2021").getTime());
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
    public void checkTrades() {

    }
}
