package scratches.boot.domainevent.book;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.SourceLocation;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * @author Rashidi Zin
 */
@ExtendWith(MockitoExtension.class)
class BookEventPublisherTests {

    private final ApplicationEventPublisher eventPublisher = mock(ApplicationEventPublisher.class);

    private final BookEventPublisher publisher = new BookEventPublisher(eventPublisher);

    @Test
    @DisplayName("Event will be triggered with information about purchased Book")
    void publishPurchasedEvent() {
        doNothing().when(eventPublisher).publishEvent(any(BookPurchaseEvent.class));

        publisher.publishPurchasedEvent(new JoinPoint() {
            @Override
            public String toShortString() {
                return null;
            }

            @Override
            public String toLongString() {
                return null;
            }

            @Override
            public Object getThis() {
                return null;
            }

            @Override
            public Object getTarget() {
                return null;
            }

            @Override
            public Object[] getArgs() {
                return new Object[]{book()};
            }

            @Override
            public Signature getSignature() {
                return null;
            }

            @Override
            public SourceLocation getSourceLocation() {
                return null;
            }

            @Override
            public String getKind() {
                return null;
            }

            @Override
            public StaticPart getStaticPart() {
                return null;
            }
        });

        ArgumentCaptor<BookPurchaseEvent> captor = ArgumentCaptor.forClass(BookPurchaseEvent.class);

        verify(eventPublisher).publishEvent(captor.capture());

        BookPurchaseEvent event = captor.getValue();

        assertThat(event.getSource()).isEqualTo(book());
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
