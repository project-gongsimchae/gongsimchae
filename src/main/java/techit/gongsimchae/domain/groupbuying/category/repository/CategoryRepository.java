package techit.gongsimchae.domain.groupbuying.category.repository;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import techit.gongsimchae.domain.groupbuying.category.entity.Category;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByName(String categoryName);

    List<Category> findAllByNameIn(List<String> names);
    Page<Category> findAllByCategoryStatus(Pageable pageable, Integer categoryStatus);
    List<Category> findAllByCategoryStatus(Integer categoryStatus);
    boolean existsByName(String categoryName);
}
