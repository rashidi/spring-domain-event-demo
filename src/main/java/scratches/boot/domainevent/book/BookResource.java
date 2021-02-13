package scratches.boot.domainevent.book;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.OK;

/**
 * @author Rashidi Zin
 */
@RestController
@AllArgsConstructor
public class BookResource {

    public final BookService service;

    @DeleteMapping("/books/{id}/purchase")
    @ResponseStatus(OK)
    public void purchase(@PathVariable Long id) {
        service.find(id).ifPresent(service::purchase);
    }

}
