package scratches.boot.domainevent.book;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import scratches.boot.domainevent.availability.BookAvailabilityRepository;

import java.util.Optional;

/**
 * @author Rashidi Zin
 */
@Service
@AllArgsConstructor
public class BookService {

    private final BookRepository repository;

    private final BookAvailabilityRepository availabilityRepository;

    @Transactional
    public void purchase(Book book) {
        repository.delete(book);

        var availability = availabilityRepository.findByIsbn(book.getIsbn());

        availability.setTotal(availability.getTotal() - 1);

        availabilityRepository.save(availability);
    }

    @Transactional(readOnly = true)
    public Optional<Book> find(Long id) {
        return repository.findById(id);
    }

}
