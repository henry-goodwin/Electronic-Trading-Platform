package com.company.View;

import javax.swing.*;
import java.awt.*;

public class OrderTablePanel extends JPanel {

    public OrderTablePanel(String tableTitle, Object[][] tableData, Object[] tableHeading) {
        // Setup Layout
        setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();

        Label activeBidsLabel = new Label(tableTitle);
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridx = 0;
        constraints.gridy = 0;
        add(activeBidsLabel, constraints);

        // Create Active Bids Table
        JTable activeBidsTable = new JTable(tableData, tableHeading);
        JScrollPane scrollPane = new JScrollPane(activeBidsTable);
        activeBidsTable.setFillsViewportHeight(true);

        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridx = 0;
        constraints.gridy = 1;
        add(activeBidsTable.getTableHeader(), constraints);

//        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.gridheight = 2;
        constraints.weighty = 1.0;   //request any extra vertical space
        constraints.anchor = GridBagConstraints.PAGE_START;
        add(activeBidsTable, constraints);
    }


}
