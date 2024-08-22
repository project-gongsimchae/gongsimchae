package techit.gongsimchae.domain.common.address.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import techit.gongsimchae.domain.common.address.entity.Address;
import techit.gongsimchae.domain.common.address.entity.DefaultAddressStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddressRespDtoWeb {
    private String zipcode;
    private String address;
    private String detailAddress;
    private String additionalAddress;
    private String UID;
    private String receiver;
    private String phoneNumber;
    private DefaultAddressStatus defaultAddressStatus;

    public AddressRespDtoWeb(Address address) {
        this.address = address.getAddress();
        this.zipcode = address.getZipcode();
        this.detailAddress = address.getDetailAddress();
        this.UID = address.getUID();
        this.receiver = address.getReceiver();
        this.phoneNumber = address.getPhoneNumber();
        this.additionalAddress = address.getAdditionalAddress();
        this.defaultAddressStatus = address.getDefaultAddressStatus();
    }
}
