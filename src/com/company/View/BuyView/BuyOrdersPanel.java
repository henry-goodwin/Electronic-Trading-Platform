package com.company.View.BuyView;

import javax.swing.*;
import java.awt.*;

public class BuyOrdersPanel extends JPanel {

    Object[][] mockData = {
            {"CPU", 12 ,300, "18/05/21" ,"active"},
            {"GPU", 15, 400, "17/05/21", "active"}
    };

    String[] columnNames = {"Component", "Quantity" ,"Bid Amount (credits)", "Date", "Status"};

    public BuyOrdersPanel() {
        // Setup Layout
        setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();

        // Add new order button and set constraints
        JButton newOrderButton = new JButton("New Buy Bid");
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridx = 0;
        constraints.weightx = 0.5;
        constraints.gridy = 0;
        add(newOrderButton, constraints);

        // Add new past orders button and set constraints
        JButton pastOrdersButton = new JButton("Past Bids");
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridx = 1;
        constraints.weightx = 0.5;
        constraints.gridy = 0;
        add(pastOrdersButton, constraints);

        Label activeBidsLabel = new Label("Active Bids");
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridx = 0;
        constraints.gridwidth = 2;
        constraints.gridy = 1;
        add(activeBidsLabel, constraints);

        // Create Active Bids Table
        JTable activeBidsTable = new JTable(mockData, columnNames);
        JScrollPane scrollPane = new JScrollPane(activeBidsTable);
        activeBidsTable.setFillsViewportHeight(true);

        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridx = 0;
        constraints.gridwidth = 2;
        constraints.gridy = 2;
        add(activeBidsTable.getTableHeader(), constraints);

//        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.gridx = 0;
        constraints.gridwidth = 2;
        constraints.gridy = 3;
        constraints.gridheight = 3;
        constraints.weighty = 1.0;   //request any extra vertical space
        constraints.anchor = GridBagConstraints.PAGE_START; //bottom of space
        add(activeBidsTable, constraints);

    }

}
