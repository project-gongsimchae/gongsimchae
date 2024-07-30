package techit.gongsimchae.global.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import techit.gongsimchae.domain.common.user.dto.FindIdVO;

import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
@Slf4j
public class CookieUtil {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static Map<String, String> loadMapFromCookie(HttpServletRequest request, String cookieName) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                log.debug("cookie: {}", cookie.getName());
                if (cookie.getName().equals(cookieName)) {
                    try {
                        String decodedValue = new String(Base64.getUrlDecoder().decode(cookie.getValue()));
                        log.debug("decoded cookie: {}", decodedValue);
                        return objectMapper.readValue(decodedValue, new TypeReference<HashMap<String, String>>() {});
                    } catch (JsonProcessingException e) {
                        log.error("json parse error", e);
                        throw new RuntimeException(e);
                    }
                }
            }
        }
        return new HashMap<>();
    }

    public static void createCookie(Map<String, String> map, String cookieName, HttpServletResponse response)  {
        String mapAsString = null;
        Cookie cookie = null;
        try {
            mapAsString = objectMapper.writeValueAsString(map);
            String encodeValue = Base64.getUrlEncoder().encodeToString(mapAsString.getBytes());
            if (cookieName.equals(FindIdVO.LOGINID_HEADER)) cookie = new Cookie(FindIdVO.LOGINID_HEADER, encodeValue);
            else cookie = new Cookie(FindIdVO.PASSWORD_HEADER, encodeValue);
            cookie.setMaxAge(60 * 5);
            response.addCookie(cookie);
        } catch (JsonProcessingException e) {
            log.error("write json object error", e);
            throw new RuntimeException(e);
        }
    }
}
