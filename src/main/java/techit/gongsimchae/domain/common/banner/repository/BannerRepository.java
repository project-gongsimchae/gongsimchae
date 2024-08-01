package techit.gongsimchae.domain.common.banner.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import techit.gongsimchae.domain.common.banner.entity.Banner;

public interface BannerRepository  extends JpaRepository<Banner, Long> {
}
