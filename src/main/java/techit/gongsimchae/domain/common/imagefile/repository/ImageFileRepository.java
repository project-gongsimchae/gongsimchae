package techit.gongsimchae.domain.common.imagefile.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import techit.gongsimchae.domain.common.imagefile.entity.ImageFile;



@Repository
public interface ImageFileRepository extends JpaRepository<ImageFile, Long> {
}
