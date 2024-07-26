package techit.gongsimchae.global.security.jwt;

public interface JwtVO {
    String HEADER = "Authorization";
    Long EXPIRES_TIME = 1000 * 60 * 60 * 24L;
    String TOKEN_PREFIX = "Bearer ";

}
