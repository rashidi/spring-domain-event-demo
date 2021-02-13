package scratches.boot.domainevent.book;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Rashidi Zin
 */
public interface BookRepository extends JpaRepository<Book, Long> {
}
