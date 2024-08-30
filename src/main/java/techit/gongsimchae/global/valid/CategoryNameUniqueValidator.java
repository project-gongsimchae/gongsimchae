package techit.gongsimchae.global.valid;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import techit.gongsimchae.domain.groupbuying.category.repository.CategoryRepository;

@RequiredArgsConstructor
@Slf4j
public class CategoryNameUniqueValidator implements ConstraintValidator<CategoryNameUnique, String> {

    private final CategoryRepository categoryRepository;

    @Override
    public void initialize(CategoryNameUnique constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if(isEmpty(s)) return false;

        return !categoryRepository.existsByName(s);
    }

    private boolean isEmpty(String value) {
        return value == null || value.isBlank();
    }
}
