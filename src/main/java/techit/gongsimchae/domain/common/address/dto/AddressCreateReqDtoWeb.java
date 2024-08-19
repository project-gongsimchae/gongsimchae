package techit.gongsimchae.domain.common.address.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import techit.gongsimchae.domain.common.user.entity.User;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddressCreateReqDtoWeb {
    private String zipcode;
    private String address;
    private String detailAddress;
    private String additionalAddress;
    private String UID;
    private String receiver;
    private String phoneNumber;

    public void applySetting(User user) {
        this.receiver = user.getName();
        this.phoneNumber = user.getPhoneNumber();
    }
}
