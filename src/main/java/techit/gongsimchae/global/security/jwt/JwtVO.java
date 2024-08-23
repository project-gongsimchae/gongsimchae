package techit.gongsimchae.global.security.jwt;

public interface JwtVO {
    String ACCESS_HEADER = "Authorization-Access";
    String REFRESH_HEADER = "Authorization-Refresh";
    Long ACCESS_TOKEN_EXPIRES_TIME = 1000 * 60 * 10L; // 10분
    Long REFRESH_TOKEN_EXPIRES_TIME = 1000 * 60 * 60 * 24 * 7L; // 1초 * 60 * 60 * 24 * 7
    String TOKEN_PREFIX = "Bearer ";
    String ACCESS_CATEGORY = "access";
    String REFRESH_CATEGORY = "refresh";

}
