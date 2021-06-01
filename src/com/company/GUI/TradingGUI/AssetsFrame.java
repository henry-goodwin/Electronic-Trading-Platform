package com.company.GUI.TradingGUI;

import com.company.Client;
import com.company.Database.Assets.AssetData;
import com.company.Database.OrgUnitAssets.OrgAssetData;
import com.company.Database.OrganisationUnit.OrganisationUnitData;
import com.company.Database.Persons.PersonsData;
import com.company.Database.Users.UsersData;
import com.company.GUI.AdminGUI.ManageUsersGUI.ManageUsersFrame;
import com.company.GUI.LoginGUI.LoginFrame;
import com.company.GUI.UserGUI.ChangePasswordFrame;
import com.company.Model.Asset;
import com.company.Model.OrgAsset;
import com.company.Model.User;
import com.company.NetworkDataSource.AssetNDS;
import com.company.NetworkDataSource.OrgAssetNDS;
import com.company.NetworkDataSource.UsersNDS;
import com.company.Utilities.PasswordHasher;

import javax.swing.*;
import java.awt.*;

public class AssetsFrame extends JFrame {

    private JPanel manageAssetsPanel;

    private JTable assetsTable;
    private OrgAssetTableModel orgAssetTableModel;

    private JButton addButton;
    private JButton editButton;
    private JButton removeButton;
    private JButton refreshTableButton;
    private JLabel nameLabel;

    private OrgAssetData orgAssetData;
    private OrganisationUnitData organisationUnitData;
    private PersonsData personsData;

    private JMenuBar adminMenuBar;
    private JMenu userMenu;
    private JMenuItem logoutButton;
    private JMenuItem passChangeButton;
    private UsersData usersData;
    private String username;

    public AssetsFrame(OrgAssetData orgAssetData, OrganisationUnitData organisationUnitData, PersonsData personsData, UsersData usersData, String username) {
        super("Manage Assets");
        this.username = username;
        setDefaultLookAndFeelDecorated(true);
        setLayout(new BorderLayout());

        adminMenuBar = new JMenuBar();
        userMenu = new JMenu("Account");

        passChangeButton = new JMenuItem("Change Password");
        passChangeButton.addActionListener(e ->checkPassword(usersData, username));
        adminMenuBar.add(userMenu);
        userMenu.add(passChangeButton);

        logoutButton = new JMenuItem("Logout");
        logoutButton.addActionListener(e -> logout());
        userMenu.add(logoutButton);

        add(adminMenuBar, BorderLayout.PAGE_START);

        this.orgAssetData = orgAssetData;
        this.organisationUnitData = organisationUnitData;
        this.personsData = personsData;

        orgAssetTableModel = new OrgAssetTableModel();
        orgAssetTableModel.setData(this.orgAssetData.getAssetList(1));

        String orgName = organisationUnitData.get(Client.getLoggedInOrgID()).getName();
        String fullName = personsData.get(Client.getLoggedInPersonID()).toString();

        String welcomeString = "Name: " + fullName + " || Organisational Unit: " + orgName;
        nameLabel = new JLabel(welcomeString);

        manageAssetsPanel = new JPanel();
        add(manageAssetsPanel, BorderLayout.CENTER);

        assetsTable = new JTable(orgAssetTableModel);
        assetsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);


        refreshTableButton = new JButton("Reload Data");
        refreshTableButton.addActionListener(e -> orgAssetTableModel.fireTableDataChanged());

        addButton = new JButton("Add Asset");
        addButton.addActionListener(e -> {
            new NewAssetFrame(new AssetData(new AssetNDS()), new OrgAssetData(new OrgAssetNDS()));
        });
        editButton = new JButton("Edit Asset");
        editButton.addActionListener(e -> editAsset());

        removeButton = new JButton("Remove Asset");

        setupLayout();
        setSize(500,500);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationByPlatform(true);
        setVisible(true);

    }

    private void setupLayout() {
        manageAssetsPanel.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();

        constraints.fill = GridBagConstraints.BOTH;
        constraints.anchor = GridBagConstraints.PAGE_START;

        constraints.gridy = 0;
        constraints.weighty = 1;
        manageAssetsPanel.add(nameLabel, constraints);

        constraints.gridy = 1;
        constraints.weighty = 1;
        manageAssetsPanel.add(new JScrollPane(assetsTable), constraints);

        constraints.gridy = 2;
        constraints.weightx = 1;
        manageAssetsPanel.add(refreshTableButton, constraints);

        constraints.gridy = 3;
        constraints.weightx = 1;
        manageAssetsPanel.add(addButton, constraints);

        constraints.gridy = 4;
        manageAssetsPanel.add(editButton, constraints);

        constraints.gridy = 5;
        manageAssetsPanel.add(removeButton, constraints);
    }

    private void logout() {
        Client.logout();

        for(Frame frame: getFrames()) {
            frame.dispose();
        }

        new LoginFrame(new UsersData(new UsersNDS()));
    }

    private void editAsset() {

        Asset asset = (Asset) orgAssetTableModel.getValueAt(assetsTable.getSelectedRow(), 0);
        Double quantity = (Double) orgAssetTableModel.getValueAt(assetsTable.getSelectedRow(), 1);

        String newQuantity = JOptionPane.showInputDialog(getContentPane(),"Enter new quantity");
        orgAssetData.updateQuantity(1, asset.getAssetID() ,Double.parseDouble(newQuantity));

    }

    private void passwordChange(String oldPassword) {

        new ChangePasswordFrame(usersData,username,oldPassword);

    }
    private void checkPassword(UsersData usersData, String username) {

        this.usersData = usersData;
        this.username=username;
        JPasswordField passField = new JPasswordField();
        String hashPassword =null;
        int option = JOptionPane.showConfirmDialog(null, passField, "Enter Password", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        //if yes is pressed
        if (option == JOptionPane.OK_OPTION) {
            // Check password matches
            hashPassword = PasswordHasher.hashString(String.valueOf(passField.getPassword()));
            if (usersData.login(username,hashPassword)) {
                passwordChange(hashPassword);
            } else {
                JOptionPane.showMessageDialog(getContentPane(), "Error: Incorrect Password");
            }
        }
        // if cancel is pressed then password won't be changed
       else if (option == JOptionPane.OK_CANCEL_OPTION) {
            JOptionPane.showMessageDialog(getContentPane(),"Password will not be changed");
        }


    }

}
