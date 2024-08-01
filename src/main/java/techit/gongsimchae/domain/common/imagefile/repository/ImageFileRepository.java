package techit.gongsimchae.domain.common.imagefile.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import techit.gongsimchae.domain.common.imagefile.entity.ImageFile;

public interface ImageFileRepository extends JpaRepository<ImageFile, Long> {
}