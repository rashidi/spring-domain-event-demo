package scratches.boot.domainevent.book;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.domain.AbstractAggregateRoot;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * @author Rashidi Zin
 */
@Data
@Entity
@EqualsAndHashCode(callSuper = true)
public class Book extends AbstractAggregateRoot<Book> {

    @Id
    @GeneratedValue
    private Long id;

    private String title;

    private String author;

    private Long isbn;

    public Book purchase() {
        registerEvent(new BookPurchaseEvent(this));
        return this;
    }

}
