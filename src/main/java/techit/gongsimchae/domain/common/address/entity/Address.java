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
    private String UID;
    private String receiver;
    private String phoneNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;


    public Address(AddressCreateReqDtoWeb addressCreateReqDtoWeb, User user) {
        this.zipcode = addressCreateReqDtoWeb.getZipcode();
        this.address = addressCreateReqDtoWeb.getAddress();
        this.detailAddress = addressCreateReqDtoWeb.getDetailAddress();
        this.phoneNumber = addressCreateReqDtoWeb.getPhoneNumber();
        this.UID = UUID.randomUUID().toString().substring(0, 8);
        this.receiver = addressCreateReqDtoWeb.getReceiver();
        this.additionalAddress = addressCreateReqDtoWeb.getAdditionalAddress();
        if(user != null) addUser(user);
    }


    public void addUser(User user) {
        this.user = user;
        user.getAddress().add(this);
    }

    public void changeInfo(AddressUpdateReqDtoWeb addressUpdateReqDtoWeb) {
        this.detailAddress = addressUpdateReqDtoWeb.getDetailAddress();
        this.receiver = addressUpdateReqDtoWeb.getReceiver();
        this.phoneNumber = addressUpdateReqDtoWeb.getPhoneNumber();
    }
}
