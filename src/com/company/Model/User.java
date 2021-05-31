package com.company.Model;

import java.io.Serializable;

public class User implements Comparable<User>, Serializable {

    private Integer userID;
    private String accountType;
    private Integer personID;
    private String username;
    private String passwordHash;

    public User() {}

    /**
     * Constructor to set values for the Person's details.
     * @param employeeID
     * @param accountType
     * @param personID
     * @param username
     * @param passwordHash
     */
    public User(Integer employeeID, String accountType, Integer personID, String username, String passwordHash) {
        this.userID = employeeID;
        this.accountType= accountType;
        this.personID = personID;
        this.username= username;
        this.passwordHash = passwordHash;
    }

    /**
     * Constructor to set values for the Person's details.
     * @param accountType
     * @param personID
     * @param username
     * @param passwordHash
     */
    public User(String accountType, Integer personID, String username, String passwordHash) {
        this.accountType= accountType;
        this.personID = personID;
        this.username= username;
        this.passwordHash = passwordHash;
    }

    /**
     * @return the employeeID
     */
    public Integer getUserID() {return this.userID;}

    /**
     * @param employeeID the employeeID to set
     */
    public void setEmployeeID(Integer employeeID) {this.userID = employeeID;}

    /**
     * @return the accountType
     */
    public String getAccountType() {return this.accountType;}

    /**
     * @param accountType the accountType to set
     */
    public void setAccountType(String accountType) {this.accountType = accountType;}

    /**
     * @return the person
     */
    public Integer getPersonID() {return this.personID;}

    /**
     * @param personID the personID to set
     */
    public void setPersonID(Integer personID) {this.personID = personID;}

    /**
     * @return the person
     */
    public String getUsername() {return this.username;}

    /**
     * @param username the username to set
     */
    public void setUsername(String username) {this.username = username;}

    /**
     * @return the passwordHash
     */
    public String getPasswordHash() {return this.passwordHash;}

    /**
     * @param passwordHash the passwordHash to set
     */
    public void setPasswordHash(String passwordHash) {this.passwordHash = passwordHash;}


    @Override
    public int compareTo(User other) {
        return this.username.compareTo(other.username);
    }
}
