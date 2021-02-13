package scratches.boot.domainevent.book;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import scratches.boot.domainevent.availability.BookAvailability;
import scratches.boot.domainevent.availability.BookAvailabilityRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

/**
 * @author Rashidi Zin
 */
@SpringBootTest(webEnvironment = RANDOM_PORT)
class BookResourceTests {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private BookRepository repository;

    @Autowired
    private BookAvailabilityRepository availabilityRepository;

    private Book book;

    @BeforeEach
    void setup() {
        book = repository.save(book());

        availabilityRepository.save(availability());
    }

    @Test
    @DisplayName("Once a book is purchased, its availability will be lesser")
    void purchase() {
        restTemplate.delete("/books/{id}/purchase", book.getId());

        BookAvailability previousAvailability = availability();
        BookAvailability currentAvailability = availabilityRepository.findByIsbn(book.getIsbn());

        assertThat(currentAvailability.getTotal())
                .describedAs("Book availability should be lesser")
                .isLessThan(previousAvailability.getTotal());
    }

    private Book book() {
        Book book = new Book();

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
