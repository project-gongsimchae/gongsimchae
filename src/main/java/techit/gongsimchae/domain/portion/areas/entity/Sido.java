package techit.gongsimchae.domain.portion.areas.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
public class Sido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String admCode;
    private String name;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;

    @OneToMany(mappedBy = "sido")
    private List<Sigungu> sigungus;
}
