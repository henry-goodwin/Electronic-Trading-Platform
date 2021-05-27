package com.company.GUI.AdminGUI.ManageUsersGUI;

import com.company.Database.Persons.PersonsData;
import com.company.Database.Users.UsersData;
import com.company.GUI.AdminGUI.AdminFrame;
import com.company.NetworkDataSource.UsersNDS;

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
        new NewUserGUI(new UsersData(new UsersNDS()), new PersonsData());
    }

    private void newPersonFrame() { new NewPersonGUI(new PersonsData()); }

}
