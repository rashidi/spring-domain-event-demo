package scratches.boot.domainevent.book;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.junit.jupiter.MockitoExtension;
import scratches.boot.domainevent.availability.BookAvailability;
import scratches.boot.domainevent.availability.BookAvailabilityRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * @author Rashidi Zin
 */
@ExtendWith(MockitoExtension.class)
class BookServiceTests {

    private final BookRepository repository = mock(BookRepository.class);

    private final BookAvailabilityRepository availabilityRepository = mock(BookAvailabilityRepository.class);

    private final BookService service = new BookService(repository, availabilityRepository);

    @Test
    @DisplayName("Total book availability will be reduced once a book is purchased")
    void purchase() {
        Book book = book();
        BookAvailability availability = availability();

        doNothing().when(repository).delete(book);

        doReturn(availability).when(availabilityRepository).findByIsbn(book.getIsbn());

        doReturn(availability).when(availabilityRepository).save(eq(availability));

        service.purchase(book);

        ArgumentCaptor<BookAvailability> captor = ArgumentCaptor.forClass(BookAvailability.class);

        verify(repository).delete(book);
        verify(availabilityRepository).findByIsbn(book.getIsbn());
        verify(availabilityRepository).save(captor.capture());

        Integer updatedTotal = captor.getValue().getTotal();

        assertThat(updatedTotal)
                .describedAs("Total availability reduced from 100 to 99")
                .isEqualTo(99);
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

    private BookAvailability availability() {
        BookAvailability availability = new BookAvailability();

        availability.setIsbn(9780385543378L);
        availability.setTotal(100);

        return availability;
    }

}
