package techit.gongsimchae.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    /**
     * 테스트용
     */
    @GetMapping("/")
    public String home(){
        return "home";
    }

}
