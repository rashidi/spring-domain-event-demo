package scratches.boot.domainevent.availability;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.junit.jupiter.MockitoExtension;
import scratches.boot.domainevent.book.Book;
import scratches.boot.domainevent.book.BookPurchaseEvent;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * @author Rashidi Zin
 */
@ExtendWith(MockitoExtension.class)
class BookAvailabilityManagementTests {

    private final BookAvailabilityRepository repository = mock(BookAvailabilityRepository.class);

    private final BookAvailabilityManagement management = new BookAvailabilityManagement(repository);

    @Test
    @DisplayName("Total book availability will be reduced once a book is purchased")
    void updateTotal() {
        BookPurchaseEvent event = new BookPurchaseEvent(book());

        doReturn(availability()).when(repository).findByIsbn(book().getIsbn());

        management.updateTotal(event);

        ArgumentCaptor<BookAvailability> captor = ArgumentCaptor.forClass(BookAvailability.class);

        verify(repository).findByIsbn(book().getIsbn());
        verify(repository).save(captor.capture());

        Integer updatedTotal = captor.getValue().getTotal();

        assertThat(updatedTotal)
                .describedAs("Total availability reduced from 100 to 99")
                .isLessThan(availability().getTotal());
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
