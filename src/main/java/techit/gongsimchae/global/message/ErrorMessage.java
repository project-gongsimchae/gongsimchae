package techit.gongsimchae.global.message;


public interface ErrorMessage {

    /**
     * common 에러
     */

    String USER_NOT_FOUND = "존재하지 않는 유저입니다.";

    /**
     * groupBuying 에러
     */

    String EVENT_TYPE_NOT_VALID = "유효하지 않은 이벤트 타입입니다.";
    String CATEGORY_NAME_NOT_VALID = "유효하지 않은 카테고리 이름입니다.";
    String EVENT_BANNER_IMAGE_EMPTY = "이벤트 배너 이미지가 존재하지 않습니다.";
    String EVENT_NOT_FOUND = "이벤트가 존재하지 않습니다.";
    String ITEM_NOT_FOUND = "아이템이 존재하지 않습니다.";

    /**
     * portion 에러
     */


    /**
     * global 에러
     */
}
