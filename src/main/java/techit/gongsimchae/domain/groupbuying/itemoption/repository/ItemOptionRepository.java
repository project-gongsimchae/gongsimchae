package techit.gongsimchae.domain.groupbuying.itemoption.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import techit.gongsimchae.domain.groupbuying.itemoption.entity.ItemOption;

import java.util.List;
import java.util.Optional;

public interface ItemOptionRepository extends JpaRepository<ItemOption,Long> {
    List<ItemOption> findAllByItemId(Long itemId);


    @Query("select io from ItemOption io join fetch io.item i where io.id = :id ")
    Optional<ItemOption> fetchItemByOptionId(@Param("id") Long itemOptionId);
}
