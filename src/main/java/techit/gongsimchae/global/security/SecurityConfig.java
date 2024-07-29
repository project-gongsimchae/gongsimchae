package techit.gongsimchae.global.security;

import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import techit.gongsimchae.domain.common.refreshtoken.service.RefreshTokenService;

import techit.gongsimchae.global.security.handler.FormAccessDeniedHandler;
import techit.gongsimchae.global.security.handler.FormAuthenticationEntryPoint;
import techit.gongsimchae.global.security.handler.OAuth2SuccessHandler;
import techit.gongsimchae.global.security.jwt.JwtAuthenticationFilter;
import techit.gongsimchae.global.security.jwt.JwtAuthorizationFilter;
import techit.gongsimchae.global.security.jwt.JwtProcess;
import techit.gongsimchae.global.security.service.CustomOauth2UserService;


import java.util.Collections;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtProcess jwtProcess;
    private final AuthenticationConfiguration authenticationConfiguration;
    private final RefreshTokenService refreshTokenService;
    private final CustomOauth2UserService oauth2UserService;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/css/**", "/js/**", "/images/**", "/fonts/**").permitAll()
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/user/**").hasRole("USER")
                        .requestMatchers("/signup", "/login", "/logout","/denied/**","/reissue").permitAll()

                        .anyRequest().permitAll())

                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)

                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .cors(cors -> cors.configurationSource(configurationSource()))

                .exceptionHandling(exception -> exception
                        .accessDeniedHandler(new FormAccessDeniedHandler("/denied"))
                        .authenticationEntryPoint(new FormAuthenticationEntryPoint()))

                .addFilterAt(new JwtAuthenticationFilter(authenticationManager(authenticationConfiguration), jwtProcess, refreshTokenService), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new JwtAuthorizationFilter(jwtProcess,refreshTokenService), JwtAuthenticationFilter.class)

                .oauth2Login(oauth -> oauth.
                        loginPage("/login")
                        .userInfoEndpoint(info -> info.
                                userService(oauth2UserService))
                        .successHandler(oAuth2SuccessHandler)
                        )

                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/")
                        .addLogoutHandler((request, response, authentication) -> {
                            for (Cookie cookie : request.getCookies()) {
                                String cookieName = cookie.getName();
                                if(cookieName.equals("view-count")) continue;
                                Cookie deleteCookie = new Cookie(cookieName, null);
                                deleteCookie.setMaxAge(0);
                                response.addCookie(deleteCookie);
                            }
                        })
                                .invalidateHttpSession(true)
                        )
        ;

        return http.build();
    }

    private CorsConfigurationSource configurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*");
        configuration.addAllowedOriginPattern("*");
        configuration.setMaxAge(3600L);
        configuration.setExposedHeaders(Collections.singletonList("Set-Cookie"));
        configuration.setExposedHeaders(Collections.singletonList("Authorization"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;

    }
}
