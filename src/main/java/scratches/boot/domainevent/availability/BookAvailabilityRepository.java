package scratches.boot.domainevent.availability;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Rashidi Zin
 */
public interface BookAvailabilityRepository extends JpaRepository<BookAvailability, Long> {

    BookAvailability findByIsbn(Long isbn);

}
