package find_driver.repositories;

import org.springframework.data.repository.CrudRepository;

import find_driver.models.DriverPosition;

import java.util.List;

public interface DriverPositionRepository extends CrudRepository<DriverPosition, Long> {
    DriverPosition findByDriverId(long driverId);
}
