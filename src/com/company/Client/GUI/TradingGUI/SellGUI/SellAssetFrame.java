package com.company.Client.GUI.TradingGUI.SellGUI;

import com.company.Client.Client;
import com.company.Client.Data.BidData;
import com.company.Client.Data.OrgAssetData;
import com.company.Client.GUI.TradingGUI.OrgAssetTableModel;
import com.company.Common.Model.Bid;
import com.company.Common.Model.OrgAsset;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class SellAssetFrame extends JFrame implements KeyListener, ActionListener {

    private JPanel sellPanel;

    private JTable assetsTable;
    private OrgAssetTableModel orgUnitAssetTableModel;
    private JTextField quantityField;
    private JTextField unitPriceField;
    private JButton placeSellOrder;
    private JLabel priceLabel;
    private JButton seePriceHistoryButton;

    private OrgAssetData orgAssetData;
    private BidData bidData;

    public SellAssetFrame(OrgAssetData orgAssetData, BidData bidData) {
        super("Place sell order");
        setDefaultLookAndFeelDecorated(true);
        setLayout(new BorderLayout());

        this.orgAssetData = orgAssetData;
        this.bidData = bidData;

        orgUnitAssetTableModel = new OrgAssetTableModel();
        try {
            orgUnitAssetTableModel.setData(this.orgAssetData.getAssetList(Client.getLoggedInOrgID()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        assetsTable = new JTable(orgUnitAssetTableModel);
        assetsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        seePriceHistoryButton = new JButton("See Asset Price History");
        seePriceHistoryButton.addActionListener(e -> seePriceHistory());

        quantityField = new JTextField();
        quantityField.addKeyListener(this);
        unitPriceField = new JTextField();
        unitPriceField.addKeyListener(this);
        placeSellOrder = new JButton("Place Sell Order");
        placeSellOrder.addActionListener(e -> sellAsset());
        priceLabel = new JLabel("Total Price:");
        sellPanel = new JPanel();
        add(sellPanel, BorderLayout.CENTER);

        setupLayout();

        setSize(700,700);
        setLocationByPlatform(true);
        setVisible(true);
    }

    private void setupLayout() {
        sellPanel.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();

        constraints.fill = GridBagConstraints.BOTH;
        constraints.anchor = GridBagConstraints.PAGE_START;

        constraints.gridy = 0;
        constraints.weighty = 2;
        constraints.weightx = 1;
        sellPanel.add(new JScrollPane(assetsTable), constraints);

        constraints.gridy = 1;
        constraints.weighty = 1;
        constraints.weightx = 1;
        sellPanel.add(seePriceHistoryButton, constraints);

        constraints.gridy = 2;
        constraints.weighty = 1;
        constraints.weightx = 1;
        sellPanel.add(new JLabel("Quantity"), constraints);

        constraints.gridy = 3;
        constraints.weighty = 1;
        constraints.weightx = 1;
        sellPanel.add(quantityField, constraints);

        constraints.gridy = 4;
        constraints.weighty = 1;
        constraints.weightx = 1;
        sellPanel.add(new JLabel("Unit Price (Credits)"), constraints);

        constraints.gridy = 5;
        constraints.weighty = 1;
        constraints.weightx = 1;
        sellPanel.add(unitPriceField, constraints);

        constraints.gridy = 6;
        constraints.weighty = 1;
        constraints.weightx = 1;
        sellPanel.add(priceLabel, constraints);

        constraints.gridy = 7;
        constraints.weighty = 1;
        constraints.weightx = 1;
        sellPanel.add(placeSellOrder, constraints);

    }

    private void sellAsset() {

        if (quantityField.getText().equals("") ||
                unitPriceField.getText().equals("")) {
            JOptionPane.showMessageDialog(getContentPane(), "Error: Please ensure all fields are valid");
        } else {
            Double quantity = Double.parseDouble(quantityField.getText());
            Double price = Double.parseDouble(unitPriceField.getText());
            OrgAsset asset = (OrgAsset) orgUnitAssetTableModel.getValueAt(assetsTable.getSelectedRow(), 0);

            if (asset.getQuantity() < quantity) {
                JOptionPane.showMessageDialog(getContentPane(), "Error: Your do not have enough units to sell");

            } else {
                Bid bid = null;
                try {
                    bid = new Bid(asset.getAssetID(), Client.getLoggedInOrgID(), "open", false, price, quantity);
                    bidData.addBid(bid);
                    JOptionPane.showMessageDialog(getContentPane(), "Successfully added new sell bid :)");
                    SellAssetFrame.this.dispose();
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(getContentPane(), e.getMessage());
                }
            }
        }

    }

    private void seePriceHistory() {

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
