package com.company.Client.GUI.AdminGUI.ManageUsersGUI;

import com.company.Client.Data.PersonsData;
import com.company.Client.Data.UsersData;
import com.company.Common.NetworkDataSource.PersonsNDS;
import com.company.Common.NetworkDataSource.UsersNDS;
import com.company.Testing.TestingException;

import javax.swing.*;
import java.awt.*;

public class ManageUsersFrame extends JFrame {

    private UsersData usersData;

    private JMenuBar menuBar;
    private JMenu newMenu;
    private JMenuItem newUser;
    private JMenuItem newPerson;

    private JPanel manageUsersPanel;


    public ManageUsersFrame(UsersData usersData) {
        super("Manage Users");
        setDefaultLookAndFeelDecorated(true);
        setLayout(new BorderLayout());
        this.usersData = usersData;

        menuBar = new JMenuBar();
        newMenu = new JMenu("New");
        newUser = new JMenuItem("New User");
        newUser.addActionListener(e -> newUserFrame());
        newPerson = new JMenuItem("New Person");
        newPerson.addActionListener(e -> newPersonFrame());
        menuBar.add(newMenu);

        newMenu.add(newUser);
        newMenu.add(newPerson);

        add(menuBar, BorderLayout.PAGE_START);

        manageUsersPanel = new JPanel();
        add(manageUsersPanel, BorderLayout.CENTER);

        setSize(500,500);
//        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationByPlatform(true);
        setVisible(true);

    }

    private void newUserFrame () {
        try {
            new NewUserGUI(new UsersData(new UsersNDS()), new PersonsData(new PersonsNDS()));
        } catch (TestingException e) {
            JOptionPane.showMessageDialog(getContentPane(), "Error");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(getContentPane(), "Error, please try again later");
        }
    }

    private void newPersonFrame() {
        try {
            new NewPersonGUI(new PersonsData(new PersonsNDS()));
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(getContentPane(), "Error, please try again later");

        }
    }

}
