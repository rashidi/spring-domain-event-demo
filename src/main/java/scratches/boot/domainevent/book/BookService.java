package scratches.boot.domainevent.book;

import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * @author Rashidi Zin
 */
@Service
@AllArgsConstructor
public class BookService {

    private final BookRepository repository;

    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public void purchase(Book book) {
        repository.delete(book);

        eventPublisher.publishEvent(new BookPurchaseEvent(book));
    }

    @Transactional(readOnly = true)
    public Optional<Book> find(Long id) {
        return repository.findById(id);
    }

}
