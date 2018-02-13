package find_driver.models;

import find_driver.utils.GeoPositionValidationException;

public class GeoPosition {
    private float _latitude;
    private float _longitude;
    private String _geoHash;
    public GeoPosition(float latitude, float longitude)
            throws GeoPositionValidationException {
        _latitude = latitude;
        _longitude = longitude;
        validateGeoPosition();
        _geoHash = _computeGeoHash();
    }

    public GeoPosition(float latitude, float longitude, String geoHash)
            throws GeoPositionValidationException {
        _latitude = latitude;
        _longitude = longitude;
        validateGeoPosition();
        _geoHash = geoHash;
    }

    public void validateGeoPosition() throws GeoPositionValidationException {
        if (_latitude < -180 || _latitude > 180) {
            throw new GeoPositionValidationException("latitude should be between -180 and 180");
        }
        if (_longitude < -90 || _longitude > 90) {
            throw new GeoPositionValidationException("longitude should be between -90 and 90");
        }
    }

    public float distance(GeoPosition another) {
        return 0;
    }

    private String _computeGeoHash() {
        return "";
    }
}
