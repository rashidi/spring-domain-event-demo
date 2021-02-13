package scratches.boot.domainevent.book;

import lombok.AllArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.OK;

/**
 * @author Rashidi Zin
 */
@Transactional
@RestController
@AllArgsConstructor
public class BookResource {

    private final BookRepository repository;

    @DeleteMapping("/books/{id}/purchase")
    @ResponseStatus(OK)
    public void purchase(@PathVariable Long id) {
        repository.findById(id).ifPresent(repository::delete);
    }

}
