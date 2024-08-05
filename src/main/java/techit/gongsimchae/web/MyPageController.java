package techit.gongsimchae.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MyPageController {
    @GetMapping("/myPage/orders")
    public String mypage(){
        return "/mypage/orders";
    }
}
