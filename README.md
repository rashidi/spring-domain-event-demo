# Spring Data: Domain Events Example
Reduce method complexity by utilising `@DomainEvents` from [Spring Data JPA](https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#core.domain-events).

## Background
In this repository we will explore Spring Data JPA helps us to adhere to [Single Responsibility](https://en.wikipedia.org/wiki/Single-responsibility_principle), a component of [SOLID Principles](https://en.wikipedia.org/wiki/SOLID).

We will reduce responsibilities of a method that does more than one thing.

## Scenario
This repository demonstrates a scenario where once a book is purchased, its total availability will be reduced.

## Implementation

### Integration End-to-end Test
In the spirit of TDD, we will start by implementing an integration end-to-end test.

```java
class BookResourceTests {

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

}
```

Full implementation can be found in [BookResourceTests.java](./src/test/java/scratches/boot/domainevent/book/BookResourceTests.java). This test will be unaffected by refactoring we will be doing through out the process.

### Entity and Repository Classes
We will start by implementing [Book.java](https://github.com/rashidi/spring-domain-event-demo/commit/921394008222574a3a6a76cadb12ec6f1cd8b3c2#diff-c18c533e8872bbe9b881fff32a3028c2872cbd89af6fd5c8bb75b79951d88534) and repository, [BookRepository.java](src/main/java/scratches/boot/domainevent/book/BookRepository.java), as well as [BookAvailability.java](src/main/java/scratches/boot/domainevent/availability/BookAvailability.java) and its repository, [BookAvailabilityRepository.java](src/main/java/scratches/boot/domainevent/availability/BookAvailabilityRepository.java).

Most of these classes will remain the same. Except for `Book` which will be affected by the refactoring.

### Service Class
Next is to implement [BookService.java](https://github.com/rashidi/spring-domain-event-demo/commit/921394008222574a3a6a76cadb12ec6f1cd8b3c2#diff-84fa43aad827356aaea34a8628e3f078f04081626827953203fe3956f7e0bee6) which will be responsible to perform deletion through `BookRepository` and update
availability through `BookAvailabilityRepository`.

```java
@Service
@AllArgsConstructor
public class BookService {

    private final BookRepository repository;

    private final BookAvailabilityRepository availabilityRepository;

    @Transactional
    public void purchase(Book book) {
        repository.delete(book);

        var availability = availabilityRepository.findByIsbn(book.getIsbn());

        availability.setTotal(availability.getTotal() - 1);

        availabilityRepository.save(availability);
    }

}
```

As we can see `BookService.purchase` does more than what it promises. It will remove a `Book` and update `BookAvailability`. Updating 
`BookAvailability` is not something its client, `BookResource`, aware about.

### REST Resource Class
[BookResource.java](https://github.com/rashidi/spring-domain-event-demo/commit/921394008222574a3a6a76cadb12ec6f1cd8b3c2#diff-f89eb0bf5a85e5a826f10e85a76be9ce3ff99173741c2af8ed2aeb03440ae217) offers an API which allows users to purchase a `Book`.

```java
@RestController
@AllArgsConstructor
public class BookResource {

    public final BookService service;

    @DeleteMapping("/books/{id}/purchase")
    @ResponseStatus(OK)
    public void purchase(@PathVariable Long id) {
        service.find(id).ifPresent(service::purchase);
    }

}
```

## Implement Spring Data's `@DomainEvents`
We will be utilising Spring Data's `@DomainEvents` to reduce responsiblity of `BookService`, or perhaps remove it totally.

### Register Event
We will start by extending `AbstractAggregateRoot` for `Book`. `AbstractAggregateRoot` helps us to simplify registration of [BookPurchaseEvent](src/main/java/scratches/boot/domainevent/book/BookPurchaseEvent.java).

```java
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
```

Now that our application is aware about `BookPurchaseEvent`. We will implement a listener which will perform subsequent action.

### Event Listener
[BookAvailabilityManagement](src/main/java/scratches/boot/domainevent/availability/BookAvailabilityManagement.java) will be listening to 
`BookPurchaseEvent`. Once the event is triggered, it will update `BookAvailability` by reducing its availability.

```java
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
```

Now we have classes that registers the event, and another that listens to the event. We will need a class to trigger `BookPurchaseEvent`.

### Trigger Event
We will refactor [BookResource](src/main/java/scratches/boot/domainevent/book/BookResource.java) to trigger `BookPurchaseEvent` by calling the method `Book.purchase()`.

```java
@Transactional
@RestController
@AllArgsConstructor
public class BookResource {

    private final BookRepository repository;

    @DeleteMapping("/books/{id}/purchase")
    @ResponseStatus(OK)
    public void purchase(@PathVariable Long id) {
        repository.findById(id).map(Book::purchase).ifPresent(repository::delete);
    }

}
```

As we can see from the implementation, `BookResource` no longer have dependency on `BookService`. Instead it relies on `BookRepository` and
let Spring Data to handle our events. Therefore, we can remove `BookService`.

We will rerun [BookResourceTests#purchase](src/test/java/scratches/boot/domainevent/book/BookResourceTests.java#L41) in order to ensure that our
refactoring did not break existing functionality.

## Step-by-step Refactoring
If you are interested to see the full refactoring process, you may do so by viewing the following commits:

  - dc248c4 - Apply event publisher and listener
  - 3da0f81 - Reduce `BookService.purchase` responsibility
  - 6936225 - Remove redundant `BookService`
  - 803a2f8 - Utilise DomainEvent