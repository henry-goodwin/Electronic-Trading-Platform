package com.company.GUI.TradingGUI;

import com.company.Client;
import com.company.Database.Bids.BidData;
import com.company.Database.OrgUnitAssets.OrgAssetData;
import com.company.Database.OrganisationUnit.OrganisationUnitData;
import com.company.Database.Persons.PersonsData;
import com.company.Database.Users.UsersData;
import com.company.GUI.LoginGUI.LoginFrame;
import com.company.GUI.TradingGUI.BuyGUI.BuyPanel;
import com.company.GUI.TradingGUI.SellGUI.SellPanel;
import com.company.GUI.UserGUI.ChangePasswordFrame;
import com.company.Model.OrganisationUnit;
import com.company.NetworkDataSource.*;
import com.company.Testing.TestingException;
import com.company.Utilities.PasswordHasher;

import javax.swing.*;
import java.awt.*;

public class TradingFrame extends JFrame {

    private JTabbedPane tabbedPane;

    private JMenuBar adminMenuBar;
    private JMenu userMenu;
    private JMenuItem logoutButton;
    private JLabel nameLabel;
    private JMenuItem passChangeButton;
    private UsersData usersData;

    private OrganisationUnitData organisationUnitData;
    private PersonsData personsData;

    public TradingFrame(OrganisationUnitData organisationUnitData, PersonsData personsData, UsersData usersData) {

        super("Trading Platform");

        this.organisationUnitData = organisationUnitData;
        this.personsData = personsData;


        adminMenuBar = new JMenuBar();
        userMenu = new JMenu("Account");

        passChangeButton = new JMenuItem("Change Password");
        passChangeButton.addActionListener(e ->checkPassword(usersData));
        adminMenuBar.add(userMenu);
        userMenu.add(passChangeButton);

        logoutButton = new JMenuItem("Logout");
        logoutButton.addActionListener(e -> logout());
        adminMenuBar.add(userMenu);
        userMenu.add(logoutButton);

        add(adminMenuBar, BorderLayout.PAGE_START);

        OrganisationUnit organisationUnit = null;
        try {
            organisationUnit = organisationUnitData.get(Client.getLoggedInOrgID());

        } catch (Exception e) {
            e.printStackTrace();
        }
        String orgName = organisationUnit.getName();
        String fullName = personsData.get(Client.getLoggedInPersonID()).toString();
        String credits = String.valueOf(organisationUnit.getCredits());

        JPanel tradingPanel = new JPanel();
        tradingPanel.setLayout(new BorderLayout());
        String welcomeString = "Name: " + fullName + " || Organisational Unit: " + orgName + " || Credits: " + credits;
        nameLabel = new JLabel(welcomeString);
        tradingPanel.add(nameLabel, BorderLayout.PAGE_START);

        tabbedPane = new JTabbedPane();
        tabbedPane.setForeground(Color.BLACK);

        JPanel assetsPanel = new AssetsPanel(new OrgAssetData(new OrgAssetNDS()));
        tabbedPane.add("Organisational Assets", assetsPanel);

        JPanel buyPanel = new BuyPanel(new BidData(new BidNDS()));
        tabbedPane.add("Buy Asset", buyPanel);

        JPanel sellPanel = new SellPanel(new BidData(new BidNDS()));
        tabbedPane.add("Sell Asset", sellPanel);

        tradingPanel.add(tabbedPane, BorderLayout.CENTER);
        add(tradingPanel, BorderLayout.CENTER);

        setSize(1000,1000);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationByPlatform(true);
        setVisible(true);
    }

    private void logout() {
        Client.logout();

        for(Frame frame: getFrames()) {
            frame.dispose();
        }

        try {
            new LoginFrame(new UsersData(new UsersNDS()));
        } catch (TestingException e) {
            JOptionPane.showMessageDialog(getContentPane(), "Logout Failed");
        }
    }

    private void passwordChange(String oldPassword) {

        new ChangePasswordFrame(usersData,oldPassword);

    }
    private void checkPassword(UsersData usersData) {

        this.usersData = usersData;
        JPasswordField passField = new JPasswordField();
        String hashPassword =null;
        int option = JOptionPane.showConfirmDialog(null, passField, "Enter Password", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        //if yes is pressed
        if (option == JOptionPane.OK_OPTION) {
            // Check password matches
            try {
                hashPassword = PasswordHasher.hashString(String.valueOf(passField.getPassword()));

                try {
                    if (usersData.checkPassword(Client.getLoggedInUserID(), hashPassword)) {
                        passwordChange(hashPassword);
                    } else {
                        JOptionPane.showMessageDialog(getContentPane(), "Error: Incorrect Password");
                    }
                } catch (TestingException e) {
                    JOptionPane.showMessageDialog(getContentPane(), "Error: Incorrect Password");
                }

            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(getContentPane(), e.getMessage());

            }
        }
        // if cancel is pressed then password won't be changed
        else if (option == JOptionPane.OK_CANCEL_OPTION) {
            JOptionPane.showMessageDialog(getContentPane(),"Password will not be changed");
        }
    }

}
