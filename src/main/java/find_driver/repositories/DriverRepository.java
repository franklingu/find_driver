package find_driver.repositories;

import org.springframework.data.repository.CrudRepository;

import find_driver.models.Driver;

public interface DriverRepository extends CrudRepository<Driver, Long> {

}
