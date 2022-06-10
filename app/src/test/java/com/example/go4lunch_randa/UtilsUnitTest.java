package com.example.go4lunch_randa;

import static org.junit.Assert.assertEquals;

import com.example.go4lunch_randa.utils.notifications.MakeMessage;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class UtilsUnitTest {
    private final List<String> workmatesList = new ArrayList<>();

    @Before
    public void setUp() {
        workmatesList.add("randa");
        workmatesList.add("maya");
        workmatesList.add("enzo");
        workmatesList.add("mirale");
        workmatesList.add("emma");
    }

    @Test
    public void testMakeMessage() {
        assertEquals(MakeMessage.makeMessage(workmatesList).toString(), "randa, maya, enzo, mirale, emma.");
    }
}