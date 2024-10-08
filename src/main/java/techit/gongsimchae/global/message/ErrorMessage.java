package techit.gongsimchae.global.message;


public interface ErrorMessage {

    /**
     * common 에러
     */
    String INQUIRY_NOT_FOUND = "존재하지 않는 문의입니다.";

    // 유저
    String USER_NOT_FOUND = "존재하지 않는 회원입니다.";

    // 주소
    String ADDRESS_NOT_FOUND = "존재하지 않는 주소입니다.";

    // 정렬
    String SORT_TYPE_NOR_FOUND = "존재하지 않는 정렬기준입니다.";

    /**
     * groupBuying 에러
     */

    // 이벤트
    String EVENT_TYPE_NOT_VALID = "유효하지 않은 이벤트 타입입니다.";
    String EVENT_BANNER_IMAGE_EMPTY = "이벤트 배너 이미지가 존재하지 않습니다.";
    String EVENT_NOT_FOUND = "이벤트가 존재하지 않습니다.";

    // 아이템
    String ITEM_NOT_FOUND = "아이템이 존재하지 않습니다.";
    String ITEM_OPTION_NOT_FOUND = "유효하지 않은 상품 옵션입니다.";

    // 카테고리
    String CATEGORY_NOT_FOUND = "존재하지 않는 카테고리입니다.";
    String CANNOT_DELETE_CATEGORY_WITH_ACTIVE_ITEM = "공동구매 진행중인 상품이 존재해 삭제할 수 없습니다.";

    // 주문 아이템
    String ORDER_ITEM_NOT_FOUND = "해당 주문아이템이 존재하지 않습니다.";

    // 쿠폰
    String COUPON_NOT_FOUND = "존재하지 않는 쿠폰입니다.";
    String COUPON_USER_ALREADY_EXIST = "이미 쿠폰을 발급받은 유저입니다.";
    String SORT_TYPE_NOT_FOUND = "존재하지 않는 정렬기준입니다.";

    // 이미지
    String IMAGE_NOT_FOUND = "존재하지 않는 이미지입니다.";


    // 오더
    String ORDERS_NOT_FOUND = "존재하지 않는 주문입니다.";

    // 주문
    String ORDER_NOT_FOUND = "존재하지 않는 주문정보입니다.";


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
