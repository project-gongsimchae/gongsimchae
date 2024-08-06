package techit.gongsimchae.domain.groupbuying.item.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import techit.gongsimchae.domain.groupbuying.category.entity.Category;
import techit.gongsimchae.domain.groupbuying.item.entity.Item;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findTop8ByOrderByCreateDateDesc();
    List<Item> findTop8ByOrderByGroupBuyingQuantityDesc();
    List<Item> findAllByCategory(Category category);
    Page<Item> findAllByCategory(Category category, Pageable pageable);
}
