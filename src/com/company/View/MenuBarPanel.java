package com.company.View;

import javax.swing.*;
import java.awt.*;

public class MenuBarPanel extends JPanel {

    public MenuBarPanel() {

        setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();

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
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridx = 0;
        constraints.gridy = 0;
        add(menuBar, constraints);

        // Create Unit Name Labels
        Label unitNameLabel = new Label("Unit Name: Sales");
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets = new Insets(10,10,10,10);  //top padding
        constraints.gridx = 0;
        constraints.weightx = 0.5;
        constraints.gridy = 1;
        add(unitNameLabel, constraints);

        Label unitCreditLabel = new Label("Credits: 350");
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridx = 1;
        constraints.weightx = 0.5;
        constraints.gridy = 1;
        add(unitCreditLabel, constraints);

    }

}
