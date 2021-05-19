package com.company.Model;

public class User {

    private Integer userID;
    private String accountType;
    private Person person;
    private String username;
    private String passwordHash;

    public User() {}

    /**
     * Constructor to set values for the Person's details.
     * @param employeeID
     * @param accountType
     * @param person
     * @param username
     * @param passwordHash
     */
    public User(Integer employeeID, String accountType, Person person, String username, String passwordHash) {
        this.userID = employeeID;
        this.accountType= accountType;
        this.person = person;
        this.username= username;
        this.passwordHash = passwordHash;
    }

    /**
     * @return the employeeID
     */
    public Integer getEmployeeID() {return this.userID;}

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
    public Person getPerson() {return this.person;}

    /**
     * @param person the person to set
     */
    public void setPerson(Person person) {this.person = person;}

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

}
