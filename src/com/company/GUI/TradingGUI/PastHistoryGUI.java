package com.company.GUI.TradingGUI;

import javax.swing.*;
import java.awt.*;

public class PastHistoryGUI extends JFrame {

    private JPanel historyPanel;

    public PastHistoryGUI() {
        super("Place buy order");
        setDefaultLookAndFeelDecorated(true);
        setLayout(new BorderLayout());

        historyPanel = new JPanel();
        add(historyPanel, BorderLayout.CENTER);

        setupLayout();

        setSize(500,500);
        setLocationByPlatform(true);
        setVisible(true);
    }

    private void setupLayout() {

    }

}
