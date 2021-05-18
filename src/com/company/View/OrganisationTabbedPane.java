package com.company.View;

import com.company.View.BuyView.BuyOrdersPanel;

import javax.swing.*;
import java.awt.*;

public class OrganisationTabbedPane extends JFrame {

    public OrganisationTabbedPane() {
        super("Electronic Trading Platform");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create Tabbed Pane
        JTabbedPane pane = new JTabbedPane();
        pane.setForeground(Color.BLACK);
        JPanel buyOrdersPanel = new BuyOrdersPanel();
        JPanel sellOrdersPanel = new SellOrdersPanel();

        // Create MenuBar Layout
        JPanel menuPanel = new MenuBarPanel();
        add(BorderLayout.NORTH, menuPanel);

        // Setup Tabs
        pane.add("Buy Orders ", buyOrdersPanel);
        pane.add("Sell Orders ", sellOrdersPanel);
        getContentPane().add(BorderLayout.CENTER, pane);


        // Display the window
//        setPreferredSize(new Dimension(1000, 1000));
        pack();
        setVisible(true);
    }

    public static void main(String[] args) {
        JFrame.setDefaultLookAndFeelDecorated(true);
        new OrganisationTabbedPane();
    }

}
