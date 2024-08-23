package techit.gongsimchae.global.valid;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import techit.gongsimchae.domain.common.user.repository.UserRepository;

@RequiredArgsConstructor
public class NicknameUniqueValidator implements ConstraintValidator<NicknameUnique, String> {
    private final UserRepository userRepository;
    @Override
    public void initialize(NicknameUnique constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if(isEmpty(s)) return false;
        return nicknameIsDuplicate(s);
    }

    private boolean isEmpty(String value) {
        return value == null || value.isBlank();
    }

    private boolean nicknameIsDuplicate(String nickname){
        return !userRepository.existsByNickname(nickname);
    }
}
