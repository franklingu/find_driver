package find_driver.models;

import javax.persistence.*;
import org.hibernate.search.annotations.*;
import java.util.Date;

@Spatial(spatialMode = SpatialMode.HASH)
@Indexed
@Entity
public class DriverPosition {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    private Long driverId;
    @Latitude
    private Double latitude;
    @Longitude
    private Double longitude;
    private Double accuracy;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDriverId() {
        return driverId;
    }

    public void setDriverId(Long driverId) {
        this.driverId = driverId;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(Double accuracy) {
        this.accuracy = accuracy;
    }
}
