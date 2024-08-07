package techit.gongsimchae.web;

import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class EventController {

    @GetMapping("/event")
    public String getEventPage(Model model){
        List<String> eventBanners = new ArrayList<>();
        eventBanners.add("https://lion-bucket-yshong.s3.ap-northeast-2.amazonaws.com/gongsimchae/event-banner/%EC%B1%84%EC%86%8C.png");
        eventBanners.add("https://lion-bucket-yshong.s3.ap-northeast-2.amazonaws.com/gongsimchae/event-banner/%EC%9C%A0%EC%A0%9C%ED%92%88.png");
        eventBanners.add("https://lion-bucket-yshong.s3.ap-northeast-2.amazonaws.com/gongsimchae/event-banner/%EB%B0%98%EB%A0%A4%EB%8F%99%EB%AC%BC.png");
        eventBanners.add("https://lion-bucket-yshong.s3.ap-northeast-2.amazonaws.com/gongsimchae/event-banner/%EA%B0%80%EC%A0%84%EC%A0%9C%ED%92%88.png");
        model.addAttribute("eventBanners", eventBanners);
        return "category/event";
    }

    @GetMapping("/event/ver2")
    public String getEventPageVer2(Model model){
        List<String> eventBanners = new ArrayList<>();
        eventBanners.add("https://lion-bucket-yshong.s3.ap-northeast-2.amazonaws.com/gongsimchae/event-banner/%EA%B0%84%EC%8B%9D%2C%EA%B3%BC%EC%9E%90%2C%EB%96%A1.png");
        eventBanners.add("https://lion-bucket-yshong.s3.ap-northeast-2.amazonaws.com/gongsimchae/event-banner/%EB%B2%A0%EC%9D%B4%EC%BB%A4%EB%A6%AC.png");
        eventBanners.add("https://lion-bucket-yshong.s3.ap-northeast-2.amazonaws.com/gongsimchae/event-banner/%EC%9C%A0%EC%95%84%EB%8F%99.png");
        eventBanners.add("https://lion-bucket-yshong.s3.ap-northeast-2.amazonaws.com/gongsimchae/event-banner/%EC%A0%95%EC%9C%A1%2C%EA%B0%80%EA%B3%B5%EC%9C%A1%2C%EA%B3%84%EB%9E%80.PNG");
        model.addAttribute("eventBanners", eventBanners);
        return "category/eventVer2";
    }
}
