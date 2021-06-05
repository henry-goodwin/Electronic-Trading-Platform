package com.company.Client;

import com.company.Client.Data.UsersData;
import com.company.Client.GUI.LoginGUI.LoginFrame;
import com.company.Common.NetworkDataSource.UsersNDS;
import com.company.Common.NetworkDataSource.*;
import com.company.Testing.TestingException;

public class Main {

    /**
     * Create the GUI.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    private static void createAndShowGUI() {

        try {
            new LoginFrame(new UsersData(new UsersNDS()));
        } catch (TestingException e) {
            System.exit(1);
        }
    }

    public static void main(String[] args) {
        createAndShowGUI();
    }
}
