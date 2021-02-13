package scratches.boot.domainevent.book;

import lombok.AllArgsConstructor;
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

    @Transactional
    public void purchase(Book book) {
        repository.delete(book);
    }

    @Transactional(readOnly = true)
    public Optional<Book> find(Long id) {
        return repository.findById(id);
    }

}
