package techit.gongsimchae.global.message;


public interface ErrorMessage {

    /**
     * common 에러
     */

    // 유저
    String USER_NOT_FOUND = "존재하지 않는 회원입니다.";

    // 주소
    String ADDRESS_NOT_FOUND = "존재하지 않는 주소입니다.";

    /**
     * groupBuying 에러
     */

    String EVENT_TYPE_NOT_VALID = "유효하지 않은 이벤트 타입입니다.";
    String CATEGORY_NAME_NOT_VALID = "유효하지 않은 카테고리 이름입니다.";
    String EVENT_BANNER_IMAGE_EMPTY = "이벤트 배너 이미지가 존재하지 않습니다.";
    String EVENT_NOT_FOUND = "이벤트가 존재하지 않습니다.";
    String ITEM_NOT_FOUND = "아이템이 존재하지 않습니다.";
    String ITEM_OPTION_NOT_FOUND = "유효하지 않은 상품 옵션입니다.";
    String CATEGORY_NOT_FOUND = "존재하지 않는 카테고리입니다.";
    String COUPON_NOT_FOUND = "존재하지 않는 쿠폰입니다.";
    String COUPON_USER_ALREADY_EXIST = "이미 쿠폰을 발급받은 유저입니다.";
    String SORT_TYPE_NOT_FOUND = "존재하지 않는 정렬기준입니다.";

    /**
     * portion 에러
     */
    // 소분글
    String SUBDIVISION_NOT_FOUND = "존재하지 않는 소분글입니다.";

    // 채팅방
    String CHATTING_ROOM_NOT_FOUND = "존재하지 않는 채팅방입니다.";

    /**
     * global 에러
     */
}
