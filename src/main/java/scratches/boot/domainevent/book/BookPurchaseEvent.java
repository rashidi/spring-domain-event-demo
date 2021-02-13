package scratches.boot.domainevent.book;

import org.springframework.context.ApplicationEvent;

/**
 * @author Rashidi Zin
 */
public class BookPurchaseEvent extends ApplicationEvent {

    private static final long serialVersionUID = 5184509297655461859L;

    /**
     * Create a new {@code ApplicationEvent}.
     *
     * @param source the object on which the event initially occurred or with
     *               which the event is associated (never {@code null})
     */
    public BookPurchaseEvent(Book source) {
        super(source);
    }

    @Override
    public Book getSource() {
        return (Book) super.getSource();
    }

}
