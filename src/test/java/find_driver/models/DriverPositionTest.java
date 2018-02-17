package find_driver.models;

import org.junit.Test;

import static org.junit.Assert.*;

public class DriverPositionTest {

    @Test
    public void testEquals() {
        DriverPosition p1 = new DriverPosition();
        DriverPosition p2 = new DriverPosition();
        assertNotEquals(p1, new Object());
        assertEquals(p1, p2);
        p1.setLatitude(1.0);
        p2.setLatitude(2.0);
        assertNotEquals(p1, p2);
        p2.setLatitude(1.0);
        assertEquals(p1, p2);
        p1.setLongitude(1.0);
        p2.setLongitude(2.0);
        assertNotEquals(p1, p2);
        p2.setLongitude(1.0);
        assertEquals(p1, p2);
        p1.setAccuracy(1.0);
        p2.setAccuracy(2.0);
        assertNotEquals(p1, p2);
        p2.setAccuracy(1.0);
        assertEquals(p1, p2);
    }
}