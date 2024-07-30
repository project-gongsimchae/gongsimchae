package techit.gongsimchae.domain;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import techit.gongsimchae.domain.common.user.dto.UserJoinReqDtoWeb;

@Embeddable
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Address {
    private String zipcode;
    private String address;
    private String detailAddress;

    public Address(UserJoinReqDtoWeb userJoinReqDtoWeb) {
        this.zipcode = userJoinReqDtoWeb.getZipcode();
        this.address = userJoinReqDtoWeb.getAddress();
        this.detailAddress = userJoinReqDtoWeb.getDetailAddress();
    }
}
