package techit.gongsimchae.domain.common.address.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import techit.gongsimchae.domain.common.address.entity.Address;

import java.util.List;
import java.util.Optional;

public interface AddressRepository extends JpaRepository<Address, Long> {
    @Query("select a from Address a join fetch a.user u where u.id = :id")
    List<Address> findAllByUser(@Param("id") Long id);

    Optional<Address> findByUID(String id);

    @Query("select a from Address a join a.user u where u.id = :id and a.defaultAddressStatus = 'PRIMARY'")
    Optional<Address> findDefaultAddressByUser(@Param("id") Long userId);
}
