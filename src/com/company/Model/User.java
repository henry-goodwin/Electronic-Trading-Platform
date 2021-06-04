package com.company.Model;

import com.company.Testing.TestingException;

import java.io.Serializable;

public class User implements Comparable<User>, Serializable {

    // Private class variables
    private Integer userID;
    private String accountType;
    private Integer personID;
    private String username;
    private String passwordHash;

    /**
     * Initialise User
     */
    public User() {}

    /**
     * Initialise User
     * @param userID userID to set
     * @param accountType accountType to set
     * @param personID personID to set
     * @param username username to set
     * @param passwordHash password has to set
     * @throws Exception Throws exception if data is invalid
     */
    public User(Integer userID, String accountType, Integer personID, String username, String passwordHash) throws TestingException {
        setUserID(userID);
        setAccountType(accountType);
        setPersonID(personID);
        setUsername(username);
        setPasswordHash(passwordHash);
    }

    /**
     * Constructor to set values for the Person's details.
     * Initialise User
     * @param accountType accountType to set
     * @param personID personID to set
     * @param username username to set
     * @param passwordHash password has to set
     * @throws TestingException Throws exception if data is invalid
     */
    public User(String accountType, Integer personID, String username, String passwordHash) throws TestingException {
        setAccountType(accountType);
        setPersonID(personID);
        setUsername(username);
        setPasswordHash(passwordHash);

    }

    /**
     * Gets username that can be used for a Table or List
     * @return Return username
     */
    public String toString() { return username; }

    /**
     * Gets the users userID
     * @return the userID
     */
    public Integer getUserID() {return this.userID;}

    /**
     * Sets the users userID
     * @param userID the userID to set
     * @throws TestingException Throws exception if userID is less than 0
     */
    public void setUserID(Integer userID) throws TestingException {
        if(userID < 0 || userID == null) {
            throw new TestingException("User ID is invalid");
        }
        this.userID = userID;
    }

    /**
     * Get users account type
     * @return the accountType
     */
    public String getAccountType() {return this.accountType;}

    /**
     * Sets the users accountType
     * @param accountType the accountType to set
     * @throws TestingException Throws exception if accountType is invalid
     */
    public void setAccountType(String accountType) throws TestingException {
        if (accountType.equals("Admin") == false && accountType.equals("Standard") == false) {
          throw new TestingException("Invalid account type");
        }
        this.accountType = accountType;
    }

    /**
     * Gets the users personID
     * @return the personID
     */
    public Integer getPersonID() {return this.personID;}

    /**
     * Sets the users personID
     * @param personID the personID to set
     * @throws TestingException Throw exception if personID is null or less than 0
     */
    public void setPersonID(Integer personID) throws TestingException {
        if (personID == null || personID < 0) {
            throw new TestingException("Invalid PersonID");
        }
        this.personID = personID;
    }

    /**
     * Gets the users username
     * @return the username
     */
    public String getUsername() {return this.username;}

    /**
     * Sets the users username
     * @param username the username to set
     * @throws TestingException Throw exception if username is invalid
     */
    public void setUsername(String username) throws TestingException {
        if (!username.matches("^[a-zA-Z0-9]([._-](?![._-])|[a-zA-Z0-9]){3,18}[a-zA-Z0-9]$")) {
            throw new TestingException("Invalid Username " + username);
        }
        this.username = username;
    }

    /**
     * Gets the users passwordHash
     * @return the passwordHash
     */
    public String getPasswordHash() {return this.passwordHash;}

    /**
     * Sets the users passwordHash
     * @param passwordHash the passwordHash to set
     * @throws TestingException Throws exception if invalid password
     */
    public void setPasswordHash(String passwordHash) throws TestingException {
        if (passwordHash.equals("") || passwordHash.equals(null)) {
            throw new TestingException("Invalid password");
        }
        this.passwordHash = passwordHash;
    }


    @Override
    public int compareTo(User other) {
        return this.username.compareTo(other.username);
    }
}
