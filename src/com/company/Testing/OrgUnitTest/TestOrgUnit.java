package com.company.Testing.OrgUnitTest;

import com.company.Model.OrganisationUnit;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

public class TestOrgUnit {

    OrganisationUnit organisationUnit;
    MockOrgUnitDatabase mockDatabase;

    /**
     * Before each test, create new Organisation Unit
     */
    @BeforeEach
    void ConstructOrganisationUnit() {
        try {
            this.organisationUnit = new OrganisationUnit();
            this.organisationUnit.setID(5);
            this.organisationUnit.setName("Sales");
            this.organisationUnit.setCredits(300.00);
            mockDatabase = new MockOrgUnitDatabase();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Test that to string returns the organisational unit string
     * @throws Exception throw exception if wrong organisational unit name is returned
     */
    @Test
    void TestToString() throws Exception {
        assertEquals("Sales", organisationUnit.toString());
    }

    /**
     * Test that getID returns the organisational unit ID
     * @throws Exception throws exception if wrong organisational unitID is returned
     */
    @Test
    void TestGetID() throws Exception {
        assertEquals(5, organisationUnit.getID());
    }

    /**
     * Test that setID sets the ID correctly
     * @throws Exception throws exception if wrong organisational unit ID is set
     */
    @Test
    void TestSetID() throws Exception {
        organisationUnit.setID(2);
        assertEquals(2, organisationUnit.getID());
    }

    /**
     * Test that getName gets the correct name
     * @throws Exception Throws exception if wrong name is returned
     */
    @Test
    void TestGetName() throws Exception {
        assertEquals("Sales", organisationUnit.getName());
    }

    /**
     * Test that exception is thrown when invalid name is entered
     * @throws Exception Throws exception if wrong name is returned
     */
    @Test
    void TestSetInvalidName() throws Exception {
        assertThrows(Exception.class, () -> {
           organisationUnit.setName("");
        });
    }

    /**
     * Test that organisational name is set correctly
     * @throws Exception
     */
    @Test
    void TestSetName() throws Exception {
        organisationUnit.setName("Service");
        assertEquals("Service", organisationUnit.getName());
    }

    /**
     * Test that getCredits gets the correct credit quantity
     * @throws Exception Throws exception if getCredits returns wrong credits
     */
    @Test
    void TestGetCredits() throws Exception {
        assertEquals(300, organisationUnit.getCredits());
    }

    /**
     * Test the setCredits sets the correct credit quantity
     * @throws Exception Throws exception if wrong credit amount is set
     */
    @Test
    void TestSetCredits() throws Exception {
        organisationUnit.setCredits(100.0);
        assertEquals(100.0, organisationUnit.getCredits());
    }

    /**
     * Tests that setCredits throws error for negative number
     * @throws Exception Throws error if a negative number is entered
     */
    @Test
    void TestNegativeSetCredits() throws Exception {
        assertThrows(Exception.class, () -> {
            organisationUnit.setCredits(-100.0);
        });
        assertEquals(300.0, organisationUnit.getCredits());
    }

    /**
     * Tests add credit function
     * @throws Exception Throws exception if wrong credits are returned
     */
    @Test
    void TestAddCredits() throws Exception {
        organisationUnit.addCredits(77.5);
        assertEquals(377.5, organisationUnit.getCredits());
    }

    /**
     * Tests add credit function
     * @throws Exception Throws exception if a negative number is entered
     */
    @Test
    void TestAddNegativeCredits() throws Exception {
        assertThrows(Exception.class, () -> {
            organisationUnit.addCredits(-50.0);
        });
    }

    /**
     * Tests remove credit function
     * @throws Exception Throws exception if wrong credits are returned
     */
    @Test
    void TestRemoveCredits() throws Exception {
        assertThrows(Exception.class, () -> {
            organisationUnit.removeCredits(500.0);
        });

        organisationUnit.removeCredits(77.5);

        assertEquals(222.5, organisationUnit.getCredits());
    }

    /**
     * Tests remove credit function
     * @throws Exception Throws exception if a number greater than the organisations credit limit is entered
     */
    @Test
    void TestTooLargeRemoveCredits() throws Exception {
        assertThrows(Exception.class, () -> {
            organisationUnit.removeCredits(500.0);
        });
    }

    @Test
    void TestAddOrgUnit_VALID() throws Exception {
        assertDoesNotThrow(() -> {
            mockDatabase.addOrganisationUnit(organisationUnit);
        });
    }

    @Test
    void TestAddOrgUnit_InvalidOrgID() throws Exception {
        assertThrows(Exception.class, () -> {
            OrganisationUnit organisationUnit = new OrganisationUnit();
            organisationUnit.setID(null);
            organisationUnit.setName("Accounting");
            organisationUnit.setCredits(200.00);

            mockDatabase.addOrganisationUnit(organisationUnit);
        });
    }

    @Test
    void TestAddOrgUnit_InvalidCredits() throws Exception {
        assertThrows(Exception.class, () -> {
            OrganisationUnit organisationUnit = new OrganisationUnit();
            organisationUnit.setID(5);
            organisationUnit.setName("name");
            organisationUnit.setCredits(null);

            mockDatabase.addOrganisationUnit(organisationUnit);
        });
    }

    /**
     * Test if get organisation unit returns the correct unit
     * @throws Exception Throws exception if orgUnit is not added correctly
     */
    @Test
    void TestGetOrganisationalUnit_Valid() throws Exception {
        mockDatabase.addOrganisationUnit(organisationUnit);
        assertEquals(organisationUnit, mockDatabase.getOrganisationUnit(organisationUnit.getID()));
    }

    /**
     * Test if get organisation unit throws exception for invalid orgID
     * @throws Exception Throws exception if orgUnit doesn't exist
     */
    @Test
    void TestGetOrganisationalUnit_Invalid() throws Exception {
        assertThrows(Exception.class, () -> {
           mockDatabase.getOrganisationUnit(8);
        });
    }

    /**
     * Test if valid organisational units update
     * @throws Exception throws exception if update fails
     */
    @Test
    void TestUpdateValidOrgUnit() throws Exception {
        OrganisationUnit organisationUnit = new OrganisationUnit();
        organisationUnit.setID(3);
        organisationUnit.setName("Finan");
        organisationUnit.setCredits(500.00);

        mockDatabase.updateOrgUnit(organisationUnit);
        assertEquals(organisationUnit.getID(), mockDatabase.getOrganisationUnit(3).getID());
        assertEquals(organisationUnit.getName(), mockDatabase.getOrganisationUnit(3).getName());
        assertEquals(organisationUnit.getCredits(), mockDatabase.getOrganisationUnit(3).getCredits());

    }

    /**
     * Test if negative update fails
     * @throws Exception throws exception if  credits are negative
     */
    @Test
    void TestUpdateNegativeCredits() throws Exception {
        assertThrows(Exception.class, () -> {
            OrganisationUnit organisationUnit = new OrganisationUnit();
            organisationUnit.setID(3);
            organisationUnit.setName("Finance");
            organisationUnit.setCredits(-500.00);

            assertThrows(Exception.class, () -> {
                mockDatabase.updateOrgUnit(organisationUnit);
            });
        });
    }

    /**
     * Test if get asset List returns the correct data
     * @throws Exception Throws exception if wrong data is returned
     */
    @Test
    void TestGetAssetList() throws Exception {

        Object[] object = mockDatabase.getList().get(0);
        OrganisationUnit organisationUnit = (OrganisationUnit) object[0];

        assertEquals("Accounting", organisationUnit.getName());
    }
}
