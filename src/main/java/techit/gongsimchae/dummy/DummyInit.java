package techit.gongsimchae.dummy;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import techit.gongsimchae.domain.common.address.repository.AddressRepository;
import techit.gongsimchae.domain.common.user.repository.UserRepository;

@Configuration
public class DummyInit extends DummyObject {
    @Bean
    @Profile("dev")
    CommandLineRunner init(UserRepository userRepository, AddressRepository addressRepository) {
        return args -> {
            userRepository.save(adminUser("admin", "admin"));
            userRepository.save(userUser("user","user"));
        };
    }

}
