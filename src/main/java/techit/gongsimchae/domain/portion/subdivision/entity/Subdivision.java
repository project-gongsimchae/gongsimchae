package techit.gongsimchae.domain.portion.subdivision.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import techit.gongsimchae.domain.BaseEntity;
import techit.gongsimchae.domain.common.imagefile.entity.ImageFile;
import techit.gongsimchae.domain.common.user.entity.User;
import techit.gongsimchae.domain.portion.subdivision.dto.SubdivisionUpdateReqDto;

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
    private Integer numOfParticipants;

    @Column(nullable = false)
    private Integer price;

    private Integer views;

    @Column(nullable = false, unique = true)
    private String UID;

    @Enumerated(EnumType.STRING)
    private SubdivisionType subdivisionType;

    private Boolean deleteStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "subdivision")
    private List<ImageFile> imageFileList;

    @Builder
    public Subdivision(String title, String content, String address, Double latitude, Double longitude, Integer numOfParticipants, Integer price, Integer views, String UID, SubdivisionType subdivisionType, User user) {
        this.title = title;
        this.content = content;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.numOfParticipants = numOfParticipants;
        this.price = price;
        this.views = views;
        this.UID = UID;
        this.subdivisionType = subdivisionType;
        this.deleteStatus = false;
        this.user = user;
    }

    public void updateSubdivision(SubdivisionUpdateReqDto subdivisionUpdateReqDto) {
        this.title = subdivisionUpdateReqDto.getTitle();
        this.content = subdivisionUpdateReqDto.getContent();
        this.address = subdivisionUpdateReqDto.getAddress();
        this.latitude = subdivisionUpdateReqDto.getLatitude();
        this.longitude = subdivisionUpdateReqDto.getLongitude();
        this.numOfParticipants = subdivisionUpdateReqDto.getNumOfParticipants();
        this.price = subdivisionUpdateReqDto.getPrice();
    }

    public void deleteSubdivision() {
        this.deleteStatus = true;
    }
}
