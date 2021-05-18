package com.company;

import javax.swing.*;
import java.awt.*;

public class OrganisationTabbedPane extends JFrame {

    public OrganisationTabbedPane() {
        super("Electronic Trading Platform");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create Tabbed Pane
        JTabbedPane pane = new JTabbedPane();
        pane.setForeground(Color.BLACK);
        JPanel homePanel = new HomePanel();
        JPanel buyOrdersPanel = new BuyOrdersPanel();
        JPanel sellOrdersPanel = new SellOrdersPanel();

        // Create the MenuBar
        JMenuBar menuBar = new JMenuBar();
        JMenu profileMenu = new JMenu("Profile");
        JMenu settingsMenu = new JMenu("Settings");
        menuBar.add(profileMenu);
        menuBar.add(settingsMenu);

        // Setup Profile Menu
        JMenuItem logoutMenuItem = new JMenuItem("Logout");
        profileMenu.add(logoutMenuItem);

        // Add menu bar
        getContentPane().add(BorderLayout.NORTH, menuBar);

        // Setup Tabs
        pane.add("Home", homePanel);
        pane.add("Buy Orders ", buyOrdersPanel);
        pane.add("Sell Orders ", sellOrdersPanel);
        getContentPane().add(BorderLayout.CENTER, pane);


        // Display the window
        setPreferredSize(new Dimension(1000, 1000));
        pack();
        setVisible(true);
    }

    public static void main(String[] args) {
        JFrame.setDefaultLookAndFeelDecorated(true);
        new OrganisationTabbedPane();
    }

}
