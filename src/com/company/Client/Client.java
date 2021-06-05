package com.company.Client;

public class Client {

    private static Integer loggedInUserID;
    private static Integer loggedInPersonID;
    private static Integer loggedInOrgID;

    public static void setUserID(Integer loggedInUserID) {Client.loggedInUserID = loggedInUserID;}
    public static void setPersonID(Integer loggedInPersonID) {Client.loggedInPersonID = loggedInPersonID;}
    public static void setOrgID(Integer orgID) {Client.loggedInOrgID = orgID;}

    public static void logout() {
        Client.loggedInUserID = null;
        Client.loggedInPersonID = null;
        Client.loggedInOrgID = null;
    }

    public static Integer getLoggedInUserID() {return loggedInUserID; }
    public static Integer getLoggedInPersonID() {return loggedInPersonID; }
    public static Integer getLoggedInOrgID() {return loggedInOrgID; }

}
