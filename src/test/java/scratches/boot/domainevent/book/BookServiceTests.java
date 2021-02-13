package scratches.boot.domainevent.book;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.*;

/**
 * @author Rashidi Zin
 */
@ExtendWith(MockitoExtension.class)
class BookServiceTests {

    private final BookRepository repository = mock(BookRepository.class);

    private final BookService service = new BookService(repository);

    @Test
    @DisplayName("Repository.delete will be triggered when Book is purchased")
    void purchase() {
        Book book = book();

        doNothing().when(repository).delete(book);

        service.purchase(book);

        verify(repository).delete(book);
    }

    @Test
    @DisplayName("Existing Book will be returned when searched by ID")
    void find() {
        Book book = book();

        doReturn(Optional.of(book)).when(repository).findById(book.getId());

        service.find(book.getId());

        verify(repository).findById(book.getId());
    }

    private Book book() {
        Book book = new Book();

        book.setId(49771934L);
        book.setTitle("Say Nothing: A True Story of Murder and Memory in Northern Ireland");
        book.setAuthor("Patrick Radden Keefe");
        book.setIsbn(9780385543378L);

        return book;
    }


}
