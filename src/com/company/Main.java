package com.company;

import com.company.Database.Assets.AssetData;
import com.company.Database.OrgUnitAssets.OrgAssetData;
import com.company.Database.OrganisationUnit.OrganisationUnitData;
import com.company.Database.Persons.PersonsData;
import com.company.Database.Users.UsersData;
import com.company.GUI.LoginGUI.LoginFrame;
import com.company.NetworkDataSource.*;
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
