package techit.gongsimchae.common;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class addressTest {

    @Test
    @DisplayName("주소가 포함되는지확인")
    void 주소_test() throws Exception {
        // given
        String sigungu = "서울 중랑구 용마산로 616";
        String address = "서울 중랑구 ";

        // when
        boolean contains = sigungu.contains(address);
        System.out.println("matches = " + contains);

        // then
    }
}
