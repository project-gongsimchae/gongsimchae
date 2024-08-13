package techit.gongsimchae.domain.groupbuying.eventcategory.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import techit.gongsimchae.domain.groupbuying.category.entity.Category;
import techit.gongsimchae.domain.groupbuying.event.entity.Event;

@Getter
@Entity
@NoArgsConstructor
public class EventCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.REMOVE)
    private Event event;
    @ManyToOne
    private Category category;

    public EventCategory(Event event, Category category) {
        this.event = event;
        this.category = category;
    }
}
