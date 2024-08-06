package techit.gongsimchae.domain.portion.areas.entity;

import jakarta.persistence.*;
import lombok.Getter;
import techit.gongsimchae.domain.BaseEntity;

import java.util.List;

@Entity
@Getter
public class SidoArea extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String admCode;
    private String name;

    @OneToMany(mappedBy = "sidoArea")
    private List<SigunguArea> sigunguAreas;
}
