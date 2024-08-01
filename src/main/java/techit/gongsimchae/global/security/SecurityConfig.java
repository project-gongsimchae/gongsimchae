package techit.gongsimchae.global.security;

import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import techit.gongsimchae.global.security.handler.FormAccessDeniedHandler;
import techit.gongsimchae.global.security.handler.FormAuthenticationEntryPoint;
import techit.gongsimchae.global.security.handler.FormAuthenticationFailureHandler;
import techit.gongsimchae.global.security.handler.FormAuthenticationSuccessHandler;
import techit.gongsimchae.global.security.service.CustomOauth2UserService;
import techit.gongsimchae.global.security.service.FormUserDetailsService;

import java.util.Collections;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    private final CustomOauth2UserService oauth2UserService;
    private final FormAuthenticationFailureHandler failureHandler;
    private final FormAuthenticationSuccessHandler successHandler;
    private final FormUserDetailsService userDetailsService;


    @Bean
    public RoleHierarchy roleHierarchy(){
        return RoleHierarchyImpl.fromHierarchy("""
                ROLE_ADMIN > ROLE_MANAGER
                ROLE_MANAGER  > ROLE_USER
                """);
    }

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
                        .requestMatchers("/user/**","/mypage/**").hasRole("USER")
                        .requestMatchers("/signup", "/login", "/logout","/denied/**","/reissue","/find/**","/emails/**").permitAll()
                        .anyRequest().permitAll())

                .formLogin(form -> form
                        .loginPage("/login")
                        .usernameParameter("loginId")
                        .successHandler(successHandler)
                        .failureHandler(failureHandler)
                        .permitAll())
                .userDetailsService(userDetailsService)

                .cors(cors -> cors.configurationSource(configurationSource()))

                .exceptionHandling(exception -> exception
                        .accessDeniedHandler(new FormAccessDeniedHandler("/denied"))
                        .authenticationEntryPoint(new FormAuthenticationEntryPoint()))


                .oauth2Login(oauth -> oauth
                        .loginPage("/login")
                        .userInfoEndpoint(info -> info.
                                userService(oauth2UserService)))

                .logout(logout -> logout

                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/main")
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
