package techit.gongsimchae.domain.groupbuying.category.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import techit.gongsimchae.domain.groupbuying.category.entity.Category;
import techit.gongsimchae.domain.groupbuying.category.repository.CategoryRepository;import techit.gongsimchae.domain.groupbuying.category.entity.Category;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    // 모든 카테고리 조회
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    // 카테고리 ID로 조회
    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id).orElse(null);
    }
}
