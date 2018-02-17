package find_driver.utils;

/** Utility class for validate a geo location.
 *
 */
public class GeoPosition {
    private double latitude;
    private double longitude;
    public GeoPosition(double latitude, double longitude)
            throws GeoPositionValidationException {
        this.latitude = latitude;
        this.longitude = longitude;
        validateGeoPosition();
    }

    public void validateGeoPosition() throws GeoPositionValidationException {
        if (latitude < -90 || latitude > 90) {
            throw new GeoPositionValidationException("latitude should be between -90 and 90");
        }
        if (longitude < -180 || longitude > 180) {
            throw new GeoPositionValidationException("longitude should be between -180 and 180");
        }
    }
}
