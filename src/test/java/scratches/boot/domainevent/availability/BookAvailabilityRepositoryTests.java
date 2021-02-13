package scratches.boot.domainevent.availability;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Rashidi Zin
 */
@DataJpaTest
class BookAvailabilityRepositoryTests {

    @Autowired
    private TestEntityManager em;

    @Autowired
    private BookAvailabilityRepository repository;

    @Test
    void findByIsbn() {
        var availability = new BookAvailability();

        availability.setIsbn(9780385543378L);
        availability.setTotal(100);

        em.persist(availability);

        assertThat(repository.findByIsbn(availability.getIsbn()))
                .isNotNull();
    }
}
