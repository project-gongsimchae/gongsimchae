package techit.gongsimchae.web;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import org.yaml.snakeyaml.emitter.Emitter;
import techit.gongsimchae.domain.groupbuying.item.entity.Item;
import techit.gongsimchae.domain.groupbuying.item.service.ItemService;
import techit.gongsimchae.domain.portion.notifications.service.NotificationService;
import techit.gongsimchae.global.dto.PrincipalDetails;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class HomeController {
    private final ItemService itemService;
    private final NotificationService notificationService;

    /**
     * 메인 페이지
     */
    @GetMapping("/main")
    public String home(Model model){
        List<Item> recentItems = itemService.getRecentItems();
        List<Item> popularItems = itemService.getPopularItems();

        model.addAttribute("recentItems", recentItems);
        model.addAttribute("popularItems", popularItems);

        return "home";
    }

    @GetMapping(value = "/subscribe", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @ResponseBody
    public SseEmitter subscribe(@RequestParam(value = "lastEventId", required = false, defaultValue = "") String lastEventId, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        return notificationService.subscribe(principalDetails, lastEventId);
    }

    @GetMapping("/")
    public String index(Model model){
        return "index";
    }

}
