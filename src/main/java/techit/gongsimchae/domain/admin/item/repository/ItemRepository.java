package techit.gongsimchae.domain.admin.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import techit.gongsimchae.domain.admin.item.entity.Item;

public interface ItemRepository extends JpaRepository<Item, Long> {
}
