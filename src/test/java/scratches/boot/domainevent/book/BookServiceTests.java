package scratches.boot.domainevent.book;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * @author Rashidi Zin
 */
@ExtendWith(MockitoExtension.class)
class BookServiceTests {

    private final BookRepository repository = mock(BookRepository.class);

    private final ApplicationEventPublisher eventPublisher = mock(ApplicationEventPublisher.class);

    private final BookService service = new BookService(repository, eventPublisher);

    @Test
    @DisplayName("Event will be triggered with information about purchased Book")
    void purchase() {
        Book book = book();

        doNothing().when(repository).delete(book);

        doNothing().when(eventPublisher).publishEvent(any(BookPurchaseEvent.class));

        service.purchase(book);

        ArgumentCaptor<BookPurchaseEvent> captor = ArgumentCaptor.forClass(BookPurchaseEvent.class);

        verify(repository).delete(book);
        verify(eventPublisher).publishEvent(captor.capture());

        BookPurchaseEvent event = captor.getValue();

        assertThat(event.getSource()).isEqualTo(book);
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
