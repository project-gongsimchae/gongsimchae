package techit.gongsimchae.domain.portion.areas.entity;

import jakarta.persistence.*;
import lombok.Getter;
import techit.gongsimchae.domain.common.user.entity.User;

import java.time.LocalDateTime;

@Entity
@Getter
public class MyeonDongEup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String admCode;
    private String name;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;

    @ManyToOne
    @JoinColumn(name = "sigungu_id")
    private Sigungu sigungu;


}
