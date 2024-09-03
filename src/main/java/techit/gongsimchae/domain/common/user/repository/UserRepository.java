package techit.gongsimchae.domain.common.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import techit.gongsimchae.domain.common.user.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>, UserCustomRepository {
    Optional<User> findByLoginId(String loginId);

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByNickname(String nickname);
    boolean existsByLoginId(String loginId);

    boolean existsByPhoneNumber(String phoneNumber);

    boolean existsByLoginIdAndEmail(String loginId, String email);

    boolean existsByNameAndEmail(String name, String email);

    Optional<User> findByNameAndEmail(String name, String email);

    Optional<User> findByNickname(String nickname);
}
