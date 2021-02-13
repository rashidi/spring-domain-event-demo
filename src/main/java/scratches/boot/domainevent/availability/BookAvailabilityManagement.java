package scratches.boot.domainevent.availability;

import lombok.AllArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import scratches.boot.domainevent.book.Book;
import scratches.boot.domainevent.book.BookPurchaseEvent;

/**
 * @author Rashidi Zin
 */
@Component
@AllArgsConstructor
public class BookAvailabilityManagement {

    private final BookAvailabilityRepository repository;

    @EventListener
    public void updateTotal(BookPurchaseEvent event) {
        Book source = event.getSource();
        BookAvailability availability = repository.findByIsbn(source.getIsbn());

        Integer updatedTotal = availability.getTotal() - 1;

        availability.setTotal(updatedTotal);

        repository.save(availability);
    }

}
