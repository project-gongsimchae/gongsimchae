package techit.gongsimchae.web;

import lombok.AllArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
public class WebRestController {

    private Environment env;

    @GetMapping("/profile")
    public String getProfile () {
        return Arrays.stream(env.getActiveProfiles())
                .filter(profile -> profile.contains("set"))  // 'set'이 포함된 프로파일만 필터링
                .collect(Collectors.joining(","));
    }
}