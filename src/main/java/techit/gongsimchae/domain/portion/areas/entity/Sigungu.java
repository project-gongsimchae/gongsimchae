package techit.gongsimchae.domain.portion.areas.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
public class Sigungu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String admCode;
    private String name;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;

    @ManyToOne
    @JoinColumn(name = "sido_id")
    private Sido sido;

    @OneToMany(mappedBy = "sigungu")
    private List<MyeonDongEup> myeonDongEups;
}
