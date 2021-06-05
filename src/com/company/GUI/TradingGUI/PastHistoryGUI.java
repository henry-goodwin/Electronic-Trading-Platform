package com.company.GUI.TradingGUI;

import com.company.Database.Assets.AssetData;
import com.company.Database.Bids.BidData;
import com.company.Database.Bids.BidDataSource;
import com.company.Model.Asset;
import com.company.Model.Bid;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.time.*;
import org.jfree.data.xy.XYDataset;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

// Code from - https://www.javaguides.net/2021/01/jfreechart-tutorial-create-charts-in-java.html
public class PastHistoryGUI extends JFrame {

    private JPanel historyPanel;
    XYDataset dataset;
    JFreeChart chart;
    ChartPanel chartPanel;

    BidData bidData;
    Integer assetID;
    AssetData assetData;

    public PastHistoryGUI(BidData bidData, AssetData assetData, Integer assetID) {
        super("Price History");
        setDefaultLookAndFeelDecorated(true);

        this.bidData = bidData;
        this.assetID = assetID;
        this.assetData = assetData;

        try {
            ArrayList<Bid> bidHistory = bidData.getBidHistory(assetID);
            Asset asset = assetData.get(assetID);
            // Code from - https://www.javaguides.net/2021/01/jfreechart-tutorial-create-charts-in-java.html
            dataset = createDataset(bidHistory);
            chart = createChart(dataset, asset.getName());
            chartPanel = new ChartPanel(chart);
            chartPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
            chartPanel.setBackground(Color.white);
            add(chartPanel);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        pack();
        setSize(700, 700);
        setLocationByPlatform(true);
        setVisible(true);
    }

    private void setupLayout() {

    }

    private XYDataset createDataset(ArrayList<Bid> bidsData) {

        var timeSeries = new TimeSeries("Price History");
        for (Bid bid: bidsData) {
            timeSeries.addOrUpdate(new Day(bid.getDate()), bid.getPrice());
        }

        TimeSeriesCollection dataset = new TimeSeriesCollection();
        dataset.addSeries(timeSeries);
        return dataset;
    }

    private JFreeChart createChart(XYDataset dataset, String assetName) {

        JFreeChart chart = ChartFactory.createTimeSeriesChart(assetName + ": Price History",
                "Date",
                "Price (Credits)",
                dataset,
                true,
                true,
                false
                );


        XYPlot plot = chart.getXYPlot();

        var renderer = new XYLineAndShapeRenderer();
        renderer.setSeriesPaint(0, Color.RED);
        renderer.setSeriesStroke(0, new BasicStroke(2.0f));

        plot.setRenderer(renderer);
        plot.setBackgroundPaint(Color.white);

        plot.setRangeGridlinesVisible(true);
        plot.setRangeGridlinePaint(Color.BLACK);

        plot.setDomainGridlinesVisible(true);
        plot.setDomainGridlinePaint(Color.BLACK);

        chart.setTitle(new TextTitle("Price History for: " + assetName,
                        new Font("Serif", java.awt.Font.BOLD, 18)
                )
        );

        return chart;
    }

}
