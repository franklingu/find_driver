package find_driver.controllers;

import find_driver.models.GeoPosition;
import find_driver.utils.GeoPositionValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
public class FindCloseDriversController {
    @RequestMapping(
            value="/drivers",
            method=RequestMethod.GET,
            produces = APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> getDrivers(
            @RequestParam(value = "latitude") float latitude,
            @RequestParam(value = "longitude") float longitude,
            @RequestParam(value = "radius", required = false) Optional<Float> radius,
            @RequestParam(value = "limit", required = false) Optional<Integer> limit) {
        GeoPosition point = null;
        try {
            point = new GeoPosition(latitude, longitude);
        } catch (GeoPositionValidationException err) {
            return new ResponseEntity<>(
                    String.format("{\"errors\":[\"%s\"]}\r\n", err.getMessage()),
                    HttpStatus.UNPROCESSABLE_ENTITY
            );
        }
        float actualRadius = 500;
        int actualLimit = 10;
        if (radius.isPresent() && radius.get() > 0) {
            actualRadius = radius.get();
        }
        if (limit.isPresent() && limit.get() > 0) {
            actualLimit = limit.get();
        }
        return new ResponseEntity<>("[]\r\n", HttpStatus.OK);
    }
}
