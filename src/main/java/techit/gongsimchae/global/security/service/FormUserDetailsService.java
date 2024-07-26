package techit.gongsimchae.global.security.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import techit.gongsimchae.domain.common.user.entity.User;
import techit.gongsimchae.domain.common.user.repository.UserRepository;
import techit.gongsimchae.global.dto.AccountDto;
import techit.gongsimchae.global.dto.PrincipalDetails;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class FormUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> _user = userRepository.findByLoginId(username);
        if (_user.isEmpty()) {
            throw new UsernameNotFoundException("not found user " + username);
        }
        AccountDto accountDto = new AccountDto(_user.get());
        return new PrincipalDetails(accountDto);
    }
}
