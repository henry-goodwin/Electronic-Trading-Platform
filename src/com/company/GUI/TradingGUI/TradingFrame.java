package com.company.GUI.TradingGUI;

import com.company.Client;
import com.company.Database.OrgUnitAssets.OrgAssetData;
import com.company.Database.OrganisationUnit.OrganisationUnitData;
import com.company.Database.Persons.PersonsData;
import com.company.Database.Users.UsersData;
import com.company.GUI.LoginGUI.LoginFrame;
import com.company.GUI.TradingGUI.Sell.SellPanel;
import com.company.NetworkDataSource.OrgAssetNDS;
import com.company.NetworkDataSource.OrganisationUnitNDS;
import com.company.NetworkDataSource.PersonsNDS;
import com.company.NetworkDataSource.UsersNDS;

import javax.swing.*;
import java.awt.*;

public class TradingFrame extends JFrame {

    private JTabbedPane tabbedPane;

    private JMenuBar adminMenuBar;
    private JMenu userMenu;
    private JMenuItem logoutButton;

    public TradingFrame() {

        super("Trading Platform");

        adminMenuBar = new JMenuBar();
        userMenu = new JMenu("Account");
        logoutButton = new JMenuItem("Logout");
        logoutButton.addActionListener(e -> logout());
        adminMenuBar.add(userMenu);
        userMenu.add(logoutButton);

        add(adminMenuBar, BorderLayout.PAGE_START);

        tabbedPane = new JTabbedPane();
        tabbedPane.setForeground(Color.BLACK);

        JPanel assetsPanel = new AssetsPanel(new OrgAssetData(new OrgAssetNDS()), new OrganisationUnitData(new OrganisationUnitNDS()), new PersonsData(new PersonsNDS()));
        tabbedPane.add("Organisational Assets", assetsPanel);

        JPanel buyPanel = new BuyPanel();
        tabbedPane.add("Buy Asset", buyPanel);

        JPanel sellPanel = new SellPanel();
        tabbedPane.add("Sell Asset", sellPanel);

        add(tabbedPane, BorderLayout.CENTER);

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

        new LoginFrame(new UsersData(new UsersNDS()));
    }

}
