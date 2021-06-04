package com.company.Testing.BidsTest;

import com.company.Model.Bid;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class TestBid {

    private Bid bid;

    @BeforeEach
    void ConstructBid() {

        DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        try {
            Date sqlDate = new Date(df.parse("04-06-2021").getTime());
            bid = new Bid(3, 2, 1, "open", true, 3.50, 300.0, 20.0, sqlDate);

        } catch (ParseException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void TestInvalidStatus() {
        assertThrows(Exception.class, () -> {
            bid.setStatus("selling");
        });
    }

    @Test
    void TestValidStatus() {
        assertDoesNotThrow(() -> {
            bid.setStatus("closed");
        });
        assertEquals("closed", bid.getStatus());
    }

}
