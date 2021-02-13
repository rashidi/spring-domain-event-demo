package scratches.boot.domainevent.book;

import lombok.AllArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

/**
 * @author Rashidi Zin
 */
@Aspect
@Component
@AllArgsConstructor
public class BookEventPublisher {

    private final ApplicationEventPublisher eventPublisher;

    @Pointcut("execution(* BookRepository.delete(..))")
    private void purchase() {
    }

    @After("purchase()")
    public void publishPurchasedEvent(JoinPoint jp) {
        Book book = (Book) jp.getArgs()[0];

        eventPublisher.publishEvent(new BookPurchaseEvent(book));
    }
}
