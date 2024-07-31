package techit.gongsimchae.domain.category.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import techit.gongsimchae.domain.category.entity.Category;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByName(String categoryName);
}
