package techit.gongsimchae.domain.common.address.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import techit.gongsimchae.domain.common.user.dto.UserAdminUpdateReqDtoWeb;
import techit.gongsimchae.domain.common.user.entity.User;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddressCreateReqDtoWeb {
    private String zipcode;
    private String address;
    private String detailAddress;
    private String additionalAddress;
    private String sigungu;
    private String receiver;
    private String phoneNumber;

    public void applySetting(String name, String phoneNumber) {
        this.receiver = name;
        this.phoneNumber = phoneNumber;
    }

    public AddressCreateReqDtoWeb(UserAdminUpdateReqDtoWeb user) {
        this.zipcode = user.getZipcode();
        this.address = user.getAddress();
        this.detailAddress = user.getDetailAddress();
        this.additionalAddress = user.getAdditionalAddress();
        this.sigungu = user.getSigungu();
    }

}
