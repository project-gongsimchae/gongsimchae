package techit.gongsimchae.domain.common.imagefile.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import techit.gongsimchae.domain.common.imagefile.entity.ImageFile;
import techit.gongsimchae.domain.common.inquiry.entity.Inquiry;
import techit.gongsimchae.domain.groupbuying.event.entity.Event;
import techit.gongsimchae.domain.groupbuying.item.entity.Item;
import techit.gongsimchae.domain.groupbuying.reviews.entity.Review;

@Repository
public interface ImageFileRepository extends JpaRepository<ImageFile, Long> {
    ImageFile findByEvent(Event event);

    List<ImageFile> findALlByEventId(Long eventId);

    List<ImageFile> findAllByItemIn(List<Item> recentItems);

    @Query("select if from ImageFile if join fetch if.item i where i.id=:id and if.itemImageFileStatus = 'THUMBNAIL' ")
    Optional<ImageFile> findByItemThumbnail(Long id);

    List<ImageFile> findAllByItem(Item item);

    ImageFile findByReview(Review review);

    List<ImageFile> findByInquiry(Inquiry inquiry);
}
