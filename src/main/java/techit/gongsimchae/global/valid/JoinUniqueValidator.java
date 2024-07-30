package techit.gongsimchae.global.valid;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import techit.gongsimchae.domain.common.user.repository.UserRepository;

@RequiredArgsConstructor
@Slf4j
public class JoinUniqueValidator implements ConstraintValidator<JoinUnique, String> {
    private final UserRepository userRepository;

    @Override
    public void initialize(JoinUnique constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if(isEmpty(s)) return false;

        return allDuplicate(s);
    }

    private boolean allDuplicate(String value) {
        return emailIsDuplicate(value) && loginIdIsDuplicate(value) && phoneNumberIsDuplicate(value);
    }

    private boolean isEmpty(String value) {
        return value == null || value.isBlank();
    }

    private boolean emailIsDuplicate(String email){
        return !userRepository.existsByEmail(email);
    }


    private boolean loginIdIsDuplicate(String loginId){
        return !userRepository.existsByLoginId(loginId);
    }

    private boolean phoneNumberIsDuplicate(String phoneNumber) {
        return !userRepository.existsByPhoneNumber(phoneNumber);
    }


}
