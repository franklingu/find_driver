package find_driver.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import find_driver.models.DriverPosition;
import find_driver.services.DriverPositionSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import find_driver.repositories.DriverPositionRepository;
import find_driver.utils.GeoPosition;
import find_driver.utils.GeoPositionValidationException;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;


class SearchResultReport {
    private long id;
    private double latitude;
    private double longitude;
    private double distance;

    SearchResultReport(){
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

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

    public void distanceFrom(double latitude, double longitude) {
        final int R = 6371; // Radius of the earth

        double latDistance = Math.toRadians(latitude - this.latitude);
        double lonDistance = Math.toRadians(longitude - this.longitude);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(latitude)) * Math.cos(Math.toRadians(this.latitude))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c * 1000; // convert to meters
        this.distance = distance;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }
}

@RestController
public class FindCloseDriversController {
    @Autowired
    private DriverPositionRepository driverPositionRepository;
    @Autowired
    private DriverPositionSearchService searchService;

    @RequestMapping(
            value="/drivers",
            method=RequestMethod.GET,
            produces = APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> getDrivers(
            @RequestParam(value = "latitude") double latitude,
            @RequestParam(value = "longitude") double longitude,
            @RequestParam(value = "radius", required = false) Optional<Float> radius,
            @RequestParam(value = "limit", required = false) Optional<Integer> limit) {
        try {
            new GeoPosition(latitude, longitude);
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
        List<DriverPosition> results = searchService.searchByLocation(
                latitude, longitude, actualRadius * 1.0, actualLimit
        );

        if (results == null) {
            return new ResponseEntity<>("[]\r\n", HttpStatus.OK);
        }
        List<SearchResultReport> ret = new ArrayList<SearchResultReport>();
        for (DriverPosition dp: results) {
            SearchResultReport srr = new SearchResultReport();
            srr.setId(dp.getDriverId());
            srr.setLatitude(dp.getLatitude());
            srr.setLongitude(dp.getLongitude());
            srr.distanceFrom(latitude, longitude);
            ret.add(srr);
        }
        return new ResponseEntity<>(ret, HttpStatus.OK);
    }
}
