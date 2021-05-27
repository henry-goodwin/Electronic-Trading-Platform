package com.company;

import com.company.Database.Users.UsersData;
import com.company.GUI.LoginGUI.LoginFrame;
import com.company.NetworkDataSource.UsersNDS;

public class Main {

    /**
     * Create the GUI.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    private static void createAndShowGUI() {
        new LoginFrame(new UsersData(new UsersNDS()));
    }

    public static void main(String[] args) {
        createAndShowGUI();
    }
}
