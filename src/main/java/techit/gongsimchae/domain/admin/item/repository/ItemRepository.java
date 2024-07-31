package techit.gongsimchae.domain.admin.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import techit.gongsimchae.domain.admin.item.entity.Item;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findTop8ByOrderByCreateDateDesc();
    List<Item> findTop8ByOrderByGroupBuyingQuantityDesc();
}
