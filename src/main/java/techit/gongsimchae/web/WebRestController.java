package techit.gongsimchae.web;

import lombok.AllArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

@RestController
@AllArgsConstructor
public class WebRestController {

    private Environment env;

    @GetMapping("/profile")
    public String getProfile () {
        String[] profiles = env.getActiveProfiles();

        if (profiles.length > 1) {
            return profiles[1];  // 두 번째 프로파일 반환
        } else {
            return profiles.length > 0 ? profiles[0] : "";
        }
    }
}