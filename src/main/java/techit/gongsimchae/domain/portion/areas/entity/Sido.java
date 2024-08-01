package techit.gongsimchae.domain.portion.areas.entity;

import jakarta.persistence.*;
import lombok.Getter;
import techit.gongsimchae.domain.BaseEntity;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
public class Sido extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String admCode;
    private String name;

    @OneToMany(mappedBy = "sido")
    private List<Sigungu> sigungus;
}
