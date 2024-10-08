package techit.gongsimchae.domain.common.address.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import techit.gongsimchae.domain.common.address.dto.AddressCreateReqDtoWeb;
import techit.gongsimchae.domain.common.address.dto.AddressUpdateReqDtoWeb;
import techit.gongsimchae.domain.common.user.entity.User;

import java.util.UUID;


@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String zipcode;
    private String address;
    private String detailAddress;
    private String additionalAddress;
    private String sigungu;
    private String UID;
    private String receiver;
    private String phoneNumber;
    @Enumerated(EnumType.STRING)
    private DefaultAddressStatus defaultAddressStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    /**
     * 앞서 저장한 주소가 없으면 이게 기본 배송지
     */
    public Address(AddressCreateReqDtoWeb addressCreateReqDtoWeb, User user, DefaultAddressStatus defaultAddressStatus) {
        this.zipcode = addressCreateReqDtoWeb.getZipcode();
        this.address = addressCreateReqDtoWeb.getAddress();
        this.detailAddress = addressCreateReqDtoWeb.getDetailAddress();
        this.sigungu = addressCreateReqDtoWeb.getSigungu();
        this.phoneNumber = addressCreateReqDtoWeb.getPhoneNumber();
        this.UID = UUID.randomUUID().toString().substring(0, 8);
        this.receiver = addressCreateReqDtoWeb.getReceiver();
        this.additionalAddress = addressCreateReqDtoWeb.getAdditionalAddress();
        this.defaultAddressStatus = defaultAddressStatus;
        this.user = user;
    }

    /**
     * 배송지 수정
     */
    public void changeInfo(AddressUpdateReqDtoWeb addressUpdateReqDtoWeb) {
        this.detailAddress = addressUpdateReqDtoWeb.getDetailAddress();
        this.receiver = addressUpdateReqDtoWeb.getReceiver();
        this.phoneNumber = addressUpdateReqDtoWeb.getPhoneNumber();
    }


    public void unsetDefaultAddress() {
        this.defaultAddressStatus = DefaultAddressStatus.SECONDARY;
    }

    public void setDefaultAddress() {
        this.defaultAddressStatus = DefaultAddressStatus.PRIMARY;
    }
}
