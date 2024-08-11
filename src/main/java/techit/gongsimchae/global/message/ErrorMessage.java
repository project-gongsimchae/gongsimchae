package techit.gongsimchae.global.message;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorMessage {

    /**
     * common 에러
     */

    /**
     * groupBuying 에러
     */

    EVENT_TYPE_NOT_VALID("유효하지 않은 이벤트 타입입니다.", "G001" ),
    CATEGORY_NAME_NOT_VALID("유효하지 않은 카테고리 이름입니다.", "G002")

    /**
     * portion 에러
     */


    /**
     * global 에러
     */
    ;
    private final String message;
    private final String customStatusCode;

}
