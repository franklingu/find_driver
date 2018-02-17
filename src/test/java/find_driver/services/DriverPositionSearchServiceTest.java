package find_driver.services;

import find_driver.models.DriverPosition;
import find_driver.repositories.DriverPositionRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DriverPositionSearchServiceTest {
    @Autowired
    private DriverPositionSearchService searchService;
    @Autowired
    private DriverPositionRepository driverPositionRepository;

    @Test
    public void testSearchByLocation() {
        List<DriverPosition> results = searchService
                .searchByLocation(45.0, 108.0, 500, 10);
        assertEquals(1, results.size());
        assertEquals(1L, results.get(0).getId().longValue());
        assertEquals(1L, results.get(0).getDriverId().longValue());
        assertEquals(45.0, results.get(0).getLatitude().doubleValue(), 0.000001);
        assertEquals(108.0, results.get(0).getLongitude().doubleValue(), 0.000001);

        results = searchService
                .searchByLocation(40.0, 108.0, 500, 10);
        assertEquals(0, results.size());
    }
}