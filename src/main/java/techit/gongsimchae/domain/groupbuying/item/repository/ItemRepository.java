package techit.gongsimchae.domain.groupbuying.item.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import techit.gongsimchae.domain.groupbuying.category.entity.Category;
import techit.gongsimchae.domain.groupbuying.item.entity.Item;

import java.util.List;
import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findTop8ByOrderByCreateDateDesc();
    List<Item> findTop8ByOrderByGroupBuyingQuantityDesc();
    List<Item> findAllByCategory(Category category);
    Page<Item> findAllByCategory(Category category, Pageable pageable);
    Optional<Item> findByUID(String id);
    Page<Item> findTop200ByOrderByCreateDateDesc(Pageable pageable);
    @Query(
            value = "SELECT * FROM (SELECT * FROM item ORDER BY create_date DESC LIMIT 200) sub ORDER BY cumulative_sales_volume DESC",
            countQuery = "SELECT COUNT(*) FROM (SELECT * FROM item ORDER BY create_date DESC LIMIT 200) sub",
            nativeQuery = true
    )
    Page<Item> findTop200ByCreateDateAndSortByCumulativeSalesVolumeDesc(Pageable pageable);

    @Query(
            value = "SELECT * FROM (SELECT * FROM item ORDER BY create_date DESC LIMIT 200) sub ORDER BY review_count DESC",
            countQuery = "SELECT COUNT(*) FROM (SELECT * FROM item ORDER BY create_date DESC LIMIT 200) sub",
            nativeQuery = true
    )
    Page<Item> findTop200ByCreateDateAndSortByReviewCountDesc(Pageable pageable);

    @Query(
            value = "SELECT * FROM (SELECT * FROM item ORDER BY create_date DESC LIMIT 200) sub ORDER BY original_price ASC",
            countQuery = "SELECT COUNT(*) FROM (SELECT * FROM item ORDER BY create_date DESC LIMIT 200) sub",
            nativeQuery = true
    )
    Page<Item> findTop200ByCreateDateAndSortByOriginalPriceAsc(Pageable pageable);

    @Query(
            value = "SELECT * FROM (SELECT * FROM item ORDER BY create_date DESC LIMIT 200) sub ORDER BY original_price DESC",
            countQuery = "SELECT COUNT(*) FROM (SELECT * FROM item ORDER BY create_date DESC LIMIT 200) sub",
            nativeQuery = true
    )
    Page<Item> findTop200ByCreateDateAndSortByOriginalPriceDesc(Pageable pageable);
}
