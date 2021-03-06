package find_driver.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import find_driver.models.Driver;
import find_driver.models.DriverPosition;
import find_driver.repositories.DriverRepository;
import find_driver.repositories.DriverPositionRepository;
import find_driver.utils.GeoPosition;
import find_driver.utils.GeoPositionValidationException;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/** Helper class to serialize request objects.
 *
 */
class DriverPositionReport {
    private double latitude;
    private double longitude;
    private double accuracy;

    public DriverPositionReport() {}

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(double accuracy) {
        this.accuracy = accuracy;
    }
}

/** Controller for drivers to report positions.
 *
 */
@RestController
public class ReportPositionController {
    @Autowired
    private DriverRepository driverRepository;
    @Autowired
    private DriverPositionRepository driverPositionRepository;

    @RequestMapping(
            value = "/drivers/{driverId}/location",
            method = RequestMethod.PUT,
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> updatePosition(
            @PathVariable(value="driverId") String driverId,
            @RequestBody DriverPositionReport location) {
        try {
            new GeoPosition(location.getLatitude(), location.getLongitude());
        } catch (GeoPositionValidationException err) {
            return new ResponseEntity<>(
                    String.format("{\"errors\":[\"%s\"]}", err.getMessage()),
                    HttpStatus.UNPROCESSABLE_ENTITY
            );
        }
        long actualDriverId;
        try {
            actualDriverId = Integer.parseInt(driverId);
        } catch (NumberFormatException err) {
            return new ResponseEntity<>(
                    String.format("{\"errors\":[\"%s\"]}", "Invalid driver id format"),
                    HttpStatus.UNPROCESSABLE_ENTITY
            );
        }
        // A quick way to check driver id exists in database or not
        //   Do use constant here as in production these will be replaced by
        //   complex and more dynamic check
        if (actualDriverId < 0 || actualDriverId > 50000) {
            return new ResponseEntity<>("{}", HttpStatus.NOT_FOUND);
        }
        // Supposedly we should check driver existence first. Due to time constraint, we will
        //   just leave it like this for now
        // Driver driver = driverRepository.findOne(actualDriverId);
        // if (driver == null) {
        //     return new ResponseEntity<>("{}\r\n", HttpStatus.NOT_FOUND);
        // }

        DriverPosition driverPosition = driverPositionRepository.findByDriverId(actualDriverId);
        if (driverPosition == null) {
            driverPosition = new DriverPosition();
        }
        driverPosition.setDriverId(actualDriverId);
        driverPosition.setLatitude(location.getLatitude());
        driverPosition.setLongitude(location.getLongitude());
        driverPosition.setAccuracy(location.getAccuracy());
        driverPositionRepository.save(driverPosition);

        return new ResponseEntity<>("{}", HttpStatus.OK);
    }
}
