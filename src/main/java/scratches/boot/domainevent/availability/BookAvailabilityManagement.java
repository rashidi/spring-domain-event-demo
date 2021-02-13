package scratches.boot.domainevent.availability;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;
import scratches.boot.domainevent.book.Book;
import scratches.boot.domainevent.book.BookPurchaseEvent;

import static org.springframework.transaction.annotation.Propagation.REQUIRES_NEW;

/**
 * @author Rashidi Zin
 */
@Component
@AllArgsConstructor
public class BookAvailabilityManagement {

    private final BookAvailabilityRepository repository;

    @TransactionalEventListener
    @Transactional(propagation = REQUIRES_NEW)
    public void updateTotal(BookPurchaseEvent event) {
        Book source = event.getSource();
        BookAvailability availability = repository.findByIsbn(source.getIsbn());

        Integer updatedTotal = availability.getTotal() - 1;

        availability.setTotal(updatedTotal);

        repository.save(availability);
    }

}
