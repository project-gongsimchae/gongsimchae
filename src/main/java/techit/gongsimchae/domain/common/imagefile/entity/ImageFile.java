package techit.gongsimchae.domain.common.imagefile.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import techit.gongsimchae.domain.BaseEntity;

@Entity
@NoArgsConstructor
@AllArgsConstructor
public class ImageFile extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String originalFilename;
    private String storeFilename;




}
