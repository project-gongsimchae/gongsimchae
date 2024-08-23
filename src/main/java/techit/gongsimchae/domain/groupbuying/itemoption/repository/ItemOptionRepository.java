package techit.gongsimchae.domain.groupbuying.itemoption.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import techit.gongsimchae.domain.groupbuying.itemoption.entity.ItemOption;

import java.util.List;
import java.util.Optional;

public interface ItemOptionRepository extends JpaRepository<ItemOption,Long> {
    List<ItemOption> findAllByItemId(Long itemId);
}
