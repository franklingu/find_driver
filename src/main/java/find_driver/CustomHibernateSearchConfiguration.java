package find_driver;

import javax.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import find_driver.services.DriverPositionSearchService;
@EnableAutoConfiguration
@Configuration
public class CustomHibernateSearchConfiguration {

    @Autowired
    private EntityManager entityManager;

    @Bean
    DriverPositionSearchService driverPositionSearchService() {
        DriverPositionSearchService driverPositionSearchService = new DriverPositionSearchService(entityManager);
        driverPositionSearchService.initializeHibernateSearch();
        return driverPositionSearchService;
    }
}
