package find_driver.utils;

import org.junit.Test;

import static org.junit.Assert.*;

public class GeoPositionTest {
    @Test(expected = GeoPositionValidationException.class)
    public void testLatitudeValidation() throws GeoPositionValidationException {
        new GeoPosition(-100, 90);
    }

    @Test(expected = GeoPositionValidationException.class)
    public void testLongitudeValidation() throws GeoPositionValidationException {
        new GeoPosition(-80, 200);
    }

    @Test
    public void testNoException() throws GeoPositionValidationException {
        new GeoPosition(-90, 180);
        new GeoPosition(-90, 90);
        new GeoPosition(-0, 0);
        new GeoPosition(90, -180);
        new GeoPosition(90, -90);
    }
}