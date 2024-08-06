package techit.gongsimchae.domain.portion.areas.entity;

import jakarta.persistence.*;
import lombok.Getter;
import techit.gongsimchae.domain.BaseEntity;

import java.util.List;

@Entity
@Getter
public class SigunguArea extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String admCode;
    private String name;

    @ManyToOne
    @JoinColumn(name = "sido_id")
    private SidoArea sidoArea;

    @OneToMany(mappedBy = "sigunguArea")
    private List<MyeondongeupArea> myeondongeupAreas;
}
