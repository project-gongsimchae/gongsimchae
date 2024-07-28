package techit.gongsimchae.domain;

import jakarta.persistence.Embeddable;
import lombok.Getter;

@Embeddable
@Getter
public class Address {
    private String zipcode;
    private String address;
    private String detailAddress;
}
