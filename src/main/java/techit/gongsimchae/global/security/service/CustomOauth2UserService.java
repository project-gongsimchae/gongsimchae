package techit.gongsimchae.global.security.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;
import techit.gongsimchae.domain.common.user.entity.User;
import techit.gongsimchae.domain.common.user.repository.UserRepository;
import techit.gongsimchae.global.dto.*;

import java.util.Optional;

@Component
@Slf4j
@RequiredArgsConstructor
public class CustomOauth2UserService extends DefaultOAuth2UserService {
    private final UserRepository userRepository;
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        log.debug("oAuth2User: {}", oAuth2User);
        log.debug("userRequest: {}", userRequest.getClientRegistration());

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        OAuth2Response oAuth2Response = null;

        if (registrationId.equals("naver")) {
            oAuth2Response = new NaverResponse(oAuth2User.getAttributes());
        } else if(registrationId.equals("google")) {
            oAuth2Response = new GoogleResponse(oAuth2User.getAttributes());
        } else{
            return null;
        }

        Optional<User> _user = userRepository.findByEmail(oAuth2Response.getEmail());
        if (_user.isEmpty()) {
            User savedUser = userRepository.save(new User(oAuth2Response));
            AccountDto accountDto = new AccountDto(savedUser);
            return new PrincipalDetails(accountDto, oAuth2Response.getAttributes());
        } else{
            User user = _user.get();
            user.changeOauth(oAuth2Response);
            AccountDto accountDto = new AccountDto(user);
            return new PrincipalDetails(accountDto, oAuth2Response.getAttributes());
        }
    }
}
