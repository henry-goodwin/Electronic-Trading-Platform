package com.company;

import com.company.Database.Assets.AssetData;
import com.company.Database.OrgUnitAssets.OrgAssetData;
import com.company.Database.OrganisationUnit.OrganisationUnitData;
import com.company.Database.Persons.PersonsData;
import com.company.Database.Users.UsersData;
import com.company.GUI.LoginGUI.LoginFrame;
import com.company.NetworkDataSource.*;

public class Main {

    /**
     * Create the GUI.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    private static void createAndShowGUI() {

        // Create all Data's to generate SQL Tables
        new AssetData(new AssetNDS());
        new PersonsData(new PersonsNDS());
        new UsersData(new UsersNDS());
        new OrganisationUnitData(new OrganisationUnitNDS());
        new OrgAssetData(new OrgAssetNDS());

        new LoginFrame(new UsersData(new UsersNDS()));
    }

    public static void main(String[] args) {
        createAndShowGUI();
    }
}
