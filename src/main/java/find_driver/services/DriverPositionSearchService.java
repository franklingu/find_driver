package find_driver.services;

import org.apache.lucene.search.Query;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.hibernate.search.query.dsl.Unit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.util.List;

import find_driver.models.DriverPosition;

/** Provide search feature to driver positions.
 *
 */
@Service
public class DriverPositionSearchService {


    @Autowired
    private final EntityManager entityManager;


    @Autowired
    public DriverPositionSearchService(EntityManager entityManager) {
        super();
        this.entityManager = entityManager;
    }


    public void initializeHibernateSearch() {

        try {
            FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);
            fullTextEntityManager.createIndexer().startAndWait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /** Return list of driver positions within radius of specified center
     *
     */
    @Transactional
    public List<DriverPosition> searchByLocation(
            double latitude, double longitude, double radius, int limit) {
        FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);
        QueryBuilder qb = fullTextEntityManager
                .getSearchFactory().buildQueryBuilder().forEntity(DriverPosition.class).get();
        Query luceneQuery = qb.spatial()
                .within(radius / 1000, Unit.KM)
                .ofLatitude(latitude).andLongitude(longitude).createQuery();
        javax.persistence.Query jpaQuery = fullTextEntityManager.createFullTextQuery(
                luceneQuery, DriverPosition.class
        ).setMaxResults(limit);

        List<DriverPosition> driverPositions = null;
        try {
            driverPositions = jpaQuery.getResultList();
        } catch (NoResultException nre) {
            // do nothing
        }

        return driverPositions;
    }
}
