package techit.gongsimchae.global.valid;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import techit.gongsimchae.domain.common.user.entity.User;
import techit.gongsimchae.domain.common.user.repository.UserRepository;
import techit.gongsimchae.global.dto.PrincipalDetails;
import techit.gongsimchae.global.exception.CustomWebException;

@RequiredArgsConstructor
public class UserUpdateValidator implements ConstraintValidator<UserUpdateUnique, String> {
    private final UserRepository userRepository;
    @Override
    public void initialize(UserUpdateUnique constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if(isEmpty(s)) return false;

        PrincipalDetails principal = (PrincipalDetails) SecurityContextHolder.getContextHolderStrategy().getContext().getAuthentication().getPrincipal();
        User user = userRepository.findByLoginId(principal.getUsername()).orElseThrow(() -> new CustomWebException("not found user"));

        return allDuplicate(s, user);
    }

    private boolean allDuplicate(String value, User user) {
        return emailIsDuplicate(value,user) && phoneNumberIsDuplicate(value,user) && nicknameIsDuplicate(value, user);
    }

    private boolean isEmpty(String value) {
        return value == null || value.isBlank();
    }

    private boolean emailIsDuplicate(String email,User user){
        return user.isUserEmail(email) || !userRepository.existsByEmail(email);
    }

    private boolean phoneNumberIsDuplicate(String phoneNumber,User user) {
        return user.isUserPhoneNumber(phoneNumber) || !userRepository.existsByPhoneNumber(phoneNumber);
    }
    private boolean nicknameIsDuplicate(String nickname,User user) {
        return user.isUserNickname(nickname) || !userRepository.existsByNickname(nickname);
    }

}
