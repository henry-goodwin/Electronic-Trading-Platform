package com.company.GUI.BuyGUI;

import com.company.GUI.OrderTablePanel;

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

        OrderTablePanel tablePanel = new OrderTablePanel("Active Bids", mockData, columnNames);
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridwidth = 2;
        constraints.weightx = 1;
        constraints.gridx = 0;
        constraints.weighty = 1.0;   //request any extra vertical space
        constraints.gridy = 1;
        add(tablePanel, constraints);



    }

}
