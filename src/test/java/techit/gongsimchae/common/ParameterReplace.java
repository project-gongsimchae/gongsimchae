//package techit.gongsimchae.common;
//
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//
//public class ParameterReplace {
//
//
//    @Test
//    @DisplayName("파라미터 이름 없애기")
//    public void parameterReplace() {
//        // 첫 번째 경우: 여러 파라미터가 있는 경우
//        String url1 = "?onSale=true&type=new&page=5";
//        String url3 = "?onSale=true&page=5";
//        String url2 = "?page=52";  // 두 번째 경우: 단독으로 page=숫자만 있는 경우
//
//        System.out.println("Case 1 (Multiple parameters): " + removePageParam(url1));
//        System.out.println("Case 2 (Only page=숫자): " + removePageParam(url2));
//        System.out.println("removePageParam(url3) = " + removePageParam(url3));
//    }
//
//    public static String removePageParam(String url) {
//        // 1. page=숫자 가 단독으로 있을 때는 빈 문자열 반환
//        if (url.matches("page=\\d+")) {
//            return "";
//        }
//
//        // 2. 여러 파라미터가 있을 때 page=숫자만 제거
//        String modifiedUrl = url.replaceAll("[&?]page=\\d+", "");
//
//        return modifiedUrl;
//    }
//
//}
