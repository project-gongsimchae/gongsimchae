package techit.gongsimchae.common;

import org.junit.jupiter.api.Test;

public class SubStringAddress {

    @Test
    void SubStringAddress() throws Exception {
        // given
        String address = "서울 성동구 용답동 229-3";
        String address2 = "서울 동대문구 장안동 472-3";

        // when
        int i = address.lastIndexOf(" ");
        int i2 = address2.lastIndexOf(" ");
        address = address.substring(0,i);
        address2 = address2.substring(0,i2);
        System.out.println("address = " + address);
        System.out.println("address2 = " + address2);



        // then
    }
}
