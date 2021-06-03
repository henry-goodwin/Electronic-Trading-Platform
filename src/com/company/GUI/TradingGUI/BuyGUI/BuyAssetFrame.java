package com.company.GUI.TradingGUI.BuyGUI;

import com.company.Client;
import com.company.Database.Assets.AssetData;
import com.company.Database.Bids.BidData;
import com.company.Database.OrganisationUnit.OrganisationUnitData;
import com.company.GUI.TradingGUI.SellGUI.SellAssetFrame;
import com.company.Model.Asset;
import com.company.Model.Bid;
import com.company.Model.OrgAsset;
import com.company.Model.OrganisationUnit;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class BuyAssetFrame extends JFrame implements KeyListener, ActionListener {

    private JPanel buyPanel;

    private JList assetsList;
    private JTextField quantityField;
    private JTextField unitPriceField;
    private JLabel priceLabel;
    private JButton placeBuyOrderBtn;
    private JLabel creditsLabel;

    private AssetData assetData;
    private BidData bidData;
    private OrganisationUnitData organisationUnitData;

    private Double credits;

    public BuyAssetFrame(AssetData assetData, BidData bidData, OrganisationUnitData organisationUnitData) {
        super("Place buy order");
        setDefaultLookAndFeelDecorated(true);
        setLayout(new BorderLayout());

        this.assetData = assetData;
        this.bidData = bidData;
        this.organisationUnitData = organisationUnitData;

        assetsList = new JList(assetData.getAssetModel());
        quantityField = new JTextField();
        quantityField.addKeyListener(this);
        unitPriceField = new JTextField();
        unitPriceField.addKeyListener(this);
        priceLabel = new JLabel("Total Price (Credits): ");
        placeBuyOrderBtn = new JButton("Place Buy Order");
        placeBuyOrderBtn.addActionListener(e -> buyAsset());

        try {
            credits = organisationUnitData.get(Client.getLoggedInOrgID()).getCredits();
        } catch (Exception e) {
            e.printStackTrace();
        }
        creditsLabel = new JLabel("Credits Available: " + credits);

        buyPanel = new JPanel();
        add(buyPanel, BorderLayout.CENTER);

        setupLayout();

        setSize(500,500);
        setLocationByPlatform(true);
        setVisible(true);

    }

    private void setupLayout() {

        buyPanel.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();

        constraints.fill = GridBagConstraints.BOTH;
        constraints.anchor = GridBagConstraints.PAGE_START;
        constraints.insets = new Insets(10,10,10,10);

        constraints.gridy = 0;
        constraints.weighty = 1;
        constraints.weightx = 1;
        buyPanel.add(creditsLabel, constraints);

        constraints.gridy = 1;
        constraints.weighty = 1;
        constraints.weightx = 1;
        buyPanel.add(new JScrollPane(assetsList), constraints);

        constraints.gridy = 2;
        constraints.weighty = 1;
        constraints.weightx = 1;
        buyPanel.add(new JLabel("Quantity"), constraints);

        constraints.gridy = 3;
        constraints.weighty = 1;
        constraints.weightx = 1;
        buyPanel.add(quantityField, constraints);

        constraints.gridy = 4;
        constraints.weighty = 1;
        constraints.weightx = 1;
        buyPanel.add(new JLabel("Unit Price (Credits)"), constraints);

        constraints.gridy = 5;
        constraints.weighty = 1;
        constraints.weightx = 1;
        buyPanel.add(unitPriceField, constraints);

        constraints.gridy = 6;
        constraints.weightx = 1;
        constraints.weighty = 1;
        buyPanel.add(priceLabel, constraints);

        constraints.gridy = 7;
        constraints.weighty = 1;
        constraints.weightx = 1;
        buyPanel.add(placeBuyOrderBtn, constraints);

    }

    private void buyAsset() {
        if (quantityField.getText().equals("") ||
                unitPriceField.getText().equals("") ||
                assetsList.isSelectionEmpty()) {
            JOptionPane.showMessageDialog(getContentPane(), "Error: Please ensure all fields are valid");
        } else {

            // Get org unit credits
            Double quantity = Double.parseDouble(quantityField.getText());
            Double price = Double.parseDouble(unitPriceField.getText());

            if ((quantity * price) <= credits) {

                Asset asset = (Asset) assetsList.getSelectedValue();

                Bid bid = new Bid(asset.getAssetID(), Client.getLoggedInOrgID(), "open", true, price, quantity);
                bidData.addBid(bid);
                JOptionPane.showMessageDialog(getContentPane(), "Successfully added new buy bid :)");
                BuyAssetFrame.this.dispose();
            } else {
                JOptionPane.showMessageDialog(getContentPane(), "Error: not enough credits available");

            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }

    @Override
    public void keyTyped(KeyEvent e) {


    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {
        String quantity = quantityField.getText();
        String unitPrice = unitPriceField.getText();

        if (quantity.equals("") == false && unitPrice.equals("") == false) {
            Double totalPrice = Double.parseDouble(quantity) * Double.parseDouble(unitPrice);
            priceLabel.setText("Total Price Credits: " + totalPrice);
        }
    }
}
