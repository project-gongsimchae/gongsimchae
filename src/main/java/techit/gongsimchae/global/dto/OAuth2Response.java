package techit.gongsimchae.global.dto;

import java.util.Map;

public interface OAuth2Response {
    String getProvider();
    String getProviderId();
    String getEmail();
    String getName();
    Map<String, Object> getAttributes();
    String getLoginId();
}
