package techit.gongsimchae.web;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import techit.gongsimchae.domain.admin.item.entity.Item;
import techit.gongsimchae.domain.admin.item.service.ItemService;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class HomeController {
    private final ItemService itemService;

    /**
     * 테스트용
     */
    @GetMapping("/")
    public String home(){
        return "home";
    }
    @GetMapping("/gongu")
    public String home(Model model){
        List<Item> recentItems = itemService.getRecentItems();
        List<Item> popularItems = itemService.getPopularItems();

        model.addAttribute("recentItems", recentItems);
        model.addAttribute("popularItems", popularItems);

        return "gongu/gongu";
    }

}
