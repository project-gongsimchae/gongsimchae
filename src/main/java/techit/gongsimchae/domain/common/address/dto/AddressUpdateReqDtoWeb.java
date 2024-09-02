package techit.gongsimchae.domain.common.address.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddressUpdateReqDtoWeb {
    @NotEmpty
    private String detailAddress;
    @NotEmpty
    private String receiver;
    @NotEmpty
    private String phoneNumber;
}
