package techit.gongsimchae.domain.groupbuying.category.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import techit.gongsimchae.domain.groupbuying.category.entity.Category;

public interface CategoryRepository extends JpaRepository<Category,Long> {
}
