package com.example.go4lunch_randa;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.example.go4lunch_randa.models.Reservation;
import com.example.go4lunch_randa.models.Workmate;

import org.junit.Before;
import org.junit.Test;

public class ModelsUnitTest {
    private Workmate workmate;
    private Reservation reservation;

    @Before
    public void setUp() {
        workmate = new Workmate("123456789", "photoURL", "esteban");
        reservation = new Reservation("22-03-21", "123456789", "000000", "rialto");
    }

    @Test
    public void testGetWorkmatesData() {
        assertEquals("123456789", workmate.getUid());
        assertEquals("esteban", workmate.getName());
        assertEquals("photoURL", workmate.getUrlPicture());
        assertFalse(workmate.isNotification());


    }

    @Test
    public void testGetReservationData() {
        assertEquals("22-03-21", reservation.getBookingDate());
        assertEquals("123456789", reservation.getUserId());
        assertEquals("000000", reservation.getRestaurantId());
        assertEquals("rialto", reservation.getRestaurantName());
    }

    @Test
    public void testSetReservationData() {

        reservation.setBookingDate("11-11-11");
        reservation.setRestaurantId("123456");
        reservation.setRestaurantName("la grange");
        reservation.setUserId("111111");

        assertEquals("11-11-11", reservation.getBookingDate());
        assertEquals("111111", reservation.getUserId());
        assertEquals("123456", reservation.getRestaurantId());
        assertEquals("la grange", reservation.getRestaurantName());

    }

    @Test
    public void testSetWorkmateData() {
        workmate.setName("pascal");
        workmate.setUid("666666");
        workmate.setUrlPicture("photo.com");
        workmate.setNotification(true);

        assertEquals("pascal", workmate.getName());
        assertEquals("666666", workmate.getUid());
        assertEquals("photo.com", workmate.getUrlPicture());
        assertTrue(workmate.isNotification());

    }

}
