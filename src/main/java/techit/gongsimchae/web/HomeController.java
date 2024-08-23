package techit.gongsimchae.web;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import techit.gongsimchae.domain.groupbuying.item.dto.ItemCardResDtoWeb;
import techit.gongsimchae.domain.groupbuying.item.service.ItemService;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class HomeController {
    private final ItemService itemService;

    /**
     * 메인 페이지
     */
    @GetMapping("/main")
    public String home(Model model){
        List<ItemCardResDtoWeb> recentItems = itemService.getRecentItems();
        List<ItemCardResDtoWeb> popularItems = itemService.getPopularItems();
        model.addAttribute("recentItems", recentItems);
        model.addAttribute("popularItems", popularItems);

        return "home";
    }

}
