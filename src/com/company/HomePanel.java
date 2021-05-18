package com.company;

import javax.swing.*;
import java.awt.*;

public class HomePanel extends JPanel {

    public HomePanel() {
        JLabel nameLabel = new JLabel("Unit Name: Sales");
        JLabel creditsLabel = new JLabel("Credits Available: 5000");
        add(nameLabel);
        add(creditsLabel);

    }

    public static void main(String[] args)
    {

        new HomePanel();

    }

}
