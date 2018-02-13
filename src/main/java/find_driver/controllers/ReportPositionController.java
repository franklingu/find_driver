package find_driver.controllers;

import find_driver.models.GeoPosition;
import find_driver.utils.GeoPositionValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

class DriverLocation {
    private float _latitude;
    private float _longitude;
    private float _accuracy;

    public DriverLocation() {}

    public DriverLocation(float latitude, float longitude, float accuracy) {
        _latitude = latitude;
        _longitude = longitude;
        _accuracy = accuracy;
    }

    public float getLatitude() {
        return _latitude;
    }

    public void setLatitude(float latitude) {
        this._latitude = latitude;
    }

    public float getLongitude() {
        return _longitude;
    }

    public void setLongitude(float longitude) {
        this._longitude = longitude;
    }

    public float getAccuracy() {
        return _accuracy;
    }

    public void setAccuracy(float accuracy) {
        this._accuracy = accuracy;
    }
}

@RestController
public class ReportPositionController {
    @RequestMapping(
            value = "/drivers/{driverId}/location",
            method = RequestMethod.PUT,
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> updatePosition(
            @PathVariable(value="driverId") String driverId,
            @RequestBody DriverLocation location) {
        GeoPosition point = null;
        try {
            point = new GeoPosition(location.getLatitude(), location.getLongitude());
        } catch (GeoPositionValidationException err) {
            return new ResponseEntity<>(
                    String.format("{\"errors\":[\"%s\"]}\r\n", err.getMessage()),
                    HttpStatus.UNPROCESSABLE_ENTITY
            );
        }
        int actualDriverId = -1;
        try {
            actualDriverId = Integer.parseInt(driverId);
        } catch (NumberFormatException err) {
            return new ResponseEntity<>(
                    String.format("{\"errors\":[\"%s\"]}\r\n", "Invalid driver id format"),
                    HttpStatus.UNPROCESSABLE_ENTITY
            );
        }
        if (actualDriverId < 0 || actualDriverId > 50000) {
            return new ResponseEntity<>("{}\r\n", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>("{}\r\n", HttpStatus.OK);
    }
}
