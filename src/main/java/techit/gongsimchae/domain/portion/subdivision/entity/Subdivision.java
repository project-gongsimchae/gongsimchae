package techit.gongsimchae.domain.portion.subdivision.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import techit.gongsimchae.domain.BaseEntity;
import techit.gongsimchae.domain.common.imagefile.entity.ImageFile;
import techit.gongsimchae.domain.common.user.entity.User;

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
    private Integer price;

    private Integer views;

    @Column(nullable = false, unique = true)
    private String UID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "image_file_id")
    private ImageFile imageFile;

    @Builder
    public Subdivision(String title, String content, String address, Integer price, Integer views, String UID, User user, ImageFile imageFile) {
        this.title = title;
        this.content = content;
        this.address = address;
        this.price = price;
        this.views = views;
        this.UID = UID;
        this.user = user;
        this.imageFile = imageFile;
    }
}
