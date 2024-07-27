package techit.gongsimchae.global.dto;

import lombok.ToString;

import java.util.Map;
@ToString
public class KakaoResponse implements OAuth2Response {
    Map<String, Object> attribute;

    public KakaoResponse(Map<String, Object> attribute) {
        this.attribute = attribute;
    }

    @Override
    public String getProvider() {
        return "kakao";
    }

    @Override
    public String getProviderId() {
        return attribute.get("id").toString();
    }

    @Override
    public String getEmail() {
        return "test@naver.com";
    }

    @Override
    public String getName() {
        Map<String, Object> properties = (Map<String, Object>) attribute.get("properties");
        return properties.get("nickname").toString();
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attribute;
    }

    @Override
    public String getLoginId() {
        return getProvider() + " " + getProviderId();
    }
}
