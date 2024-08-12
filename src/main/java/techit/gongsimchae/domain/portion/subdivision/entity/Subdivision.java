package techit.gongsimchae.domain.portion.subdivision.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import techit.gongsimchae.domain.BaseEntity;
import techit.gongsimchae.domain.common.imagefile.entity.ImageFile;
import techit.gongsimchae.domain.common.user.entity.User;

import java.util.List;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "subdivision")
public class Subdivision extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private Double latitude;

    @Column(nullable = false)
    private Double longitude;

    @Column(nullable = false)
    private Integer price;

    private Integer views;

    @Column(nullable = false, unique = true)
    private String UID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "subdivision")
    private List<ImageFile> imageFileList;

    @Builder
    public Subdivision(String title, String content, String address, Double latitude, Double longitude, Integer price, Integer views, String UID, User user) {
        this.title = title;
        this.content = content;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.price = price;
        this.views = views;
        this.UID = UID;
        this.user = user;
    }
}
