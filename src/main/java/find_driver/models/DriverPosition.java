package find_driver.models;

import javax.persistence.*;
import org.hibernate.search.annotations.*;
import java.util.Date;

/** Represent driver's position in database.
 *
 * TODO: add created to keep track of history of driver's
 *   location, or keep archive of driver's location into
 *   another table.
 */
@Spatial(spatialMode = SpatialMode.HASH)
@Indexed
@Entity
public class DriverPosition {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    // Custom constraint for current implementation
    //   each driver should have only one most updated position
    @Column(unique = true)
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

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!DriverPosition.class.isAssignableFrom(obj.getClass())) {
            return false;
        }
        final DriverPosition another = (DriverPosition)obj;
        if (id == another.getId() && id != null) {
            return true;
        }
        final double threshold = 1E-9;
        boolean latSame = ((latitude == null && another.getLatitude() == null)
                ||Math.abs(latitude - another.getLatitude()) < threshold);
        boolean longSame = ((longitude == null && another.getLongitude() == null)
                || Math.abs(longitude - another.getLongitude()) < threshold);
        boolean accuracySame = ((accuracy == null && another.getAccuracy() == null)
                || Math.abs(accuracy - another.getAccuracy()) < threshold);
        if (latSame && longSame && accuracySame) {
            return true;
        }
        return false;
    }
}
