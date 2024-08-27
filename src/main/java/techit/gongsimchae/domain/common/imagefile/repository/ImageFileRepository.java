package techit.gongsimchae.domain.common.imagefile.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import techit.gongsimchae.domain.common.imagefile.entity.ImageFile;
import techit.gongsimchae.domain.groupbuying.event.entity.Event;
import techit.gongsimchae.domain.groupbuying.item.entity.Item;
import techit.gongsimchae.domain.groupbuying.reviews.entity.Review;

@Repository
public interface ImageFileRepository extends JpaRepository<ImageFile, Long> {
    ImageFile findByEvent(Event event);

    List<ImageFile> findALlByEventId(Long eventId);

    List<ImageFile> findAllByItemIn(List<Item> recentItems);

    ImageFile findByItem(Item item);

    List<ImageFile> findAllByItem(Item item);

    ImageFile findByReview(Review review);
}
