package techit.gongsimchae.domain.portion.areas.entity;

import jakarta.persistence.*;
import lombok.Getter;
import techit.gongsimchae.domain.BaseEntity;
@Entity
@Getter
public class MyeondongeupArea extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String admCode;
    private String name;

    @ManyToOne
    @JoinColumn(name = "sigungu_id")
    private SigunguArea sigunguArea;


}
