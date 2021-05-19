package com.company.Model;

public class Person {

    private int personID;
    private String firstname;
    private String lastname;

    public Person() { }

    /**
     * Constructor to set values for the Person's details.
     * @param personID
     * @param firstname
     * @param lastname
     */
    public Person(Integer personID, String firstname, String lastname) {
        this.personID = personID;
        this.firstname= firstname;
        this.lastname = lastname;
    }

    /**
     * @return the personID
     */
    public Integer getPersonID() {return this.personID;}

    /**
     * @param personID the personID to set
     */
    public void setPersonID(Integer personID) {this.personID = personID;}

    /**
     * @return the firstname
     */
    public String getFirstname() {return this.firstname;}

    /**
     * @param firstname the lastname to set
     */
    public void setFirstname(String firstname) {this.firstname = firstname;}

    /**
     * @return the lastname
     */
    public String getLastname() {return this.lastname;}

    /**
     * @param lastname the lastname to set
     */
    public void setLastname(String lastname) {this.lastname = lastname;}
}
