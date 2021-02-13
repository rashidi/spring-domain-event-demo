package scratches.boot.domainevent.availability;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * @author Rashidi Zin
 */
@Data
@Entity
public class BookAvailability {

    @Id
    @GeneratedValue
    private Long id;

    private Long isbn;

    private Integer total;

}
