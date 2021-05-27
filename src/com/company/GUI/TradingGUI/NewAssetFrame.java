package com.company.GUI.TradingGUI;

import com.company.Database.Assets.AssetData;
import com.company.Database.Assets.AssetDataSource;
import com.company.Database.Users.UsersData;
import com.company.Model.Asset;
import com.company.NetworkDataSource.AssetNDS;

import javax.swing.*;
import java.awt.*;

public class NewAssetFrame extends JFrame {
    private JPanel newAssetsPanel;

    private JList namesList;
    private JTextField quantityField;
    private JButton addNewAssetButton;
    private JButton newNameButton;

    private AssetData assetData;
    private DefaultListModel<Asset> listModel;

    public NewAssetFrame(AssetData usersData) {
        super("New Assets");
        setDefaultLookAndFeelDecorated(true);
        setLayout(new BorderLayout());

        this.assetData = usersData;

        listModel = (DefaultListModel<Asset>) assetData.getAssetModel();
        namesList = new JList(listModel);
        quantityField = new JTextField();
        addNewAssetButton = new JButton("Add New Asset to Organisation");
        newNameButton = new JButton("Add");
        newNameButton.addActionListener(e -> newName());
        newAssetsPanel = new JPanel();
        add(newAssetsPanel, BorderLayout.CENTER);


        setupLayout();

        setSize(500,500);
        setLocationByPlatform(true);
        setVisible(true);

    }

    private void setupLayout() {
        newAssetsPanel.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();

        constraints.fill = GridBagConstraints.BOTH;
        constraints.anchor = GridBagConstraints.PAGE_START;
        constraints.weighty = 1;
        constraints.weightx = 1;

        constraints.gridy = 0;
        newAssetsPanel.add(new JScrollPane(namesList), constraints);

        constraints.gridy = 1;
        newAssetsPanel.add(newNameButton, constraints);

        constraints.gridy = 2;
        newAssetsPanel.add(new JLabel("Quantity"), constraints);

        constraints.gridy = 3;
        newAssetsPanel.add(quantityField, constraints);

        constraints.gridy = 4;
        newAssetsPanel.add(addNewAssetButton, constraints);
    }

    private void newName() {
        String name = JOptionPane.showInputDialog(getContentPane(),"Asset Name");

        // Check that name hasn't been used
        if (assetData.nameAvailability(name)) {
            assetData.addAsset(new Asset(name));
            listModel.addElement(new Asset(name));
        } else {
            JOptionPane.showMessageDialog(getContentPane(), "Error: Asset Already exists");
        }
    }

    private void addAsset() {
        if (namesList.isSelectionEmpty() || quantityField.getText().equals("")) {
            JOptionPane.showMessageDialog(getContentPane(), "Error: Please ensure all fields are valid");
        } else {

        }
    }

}
