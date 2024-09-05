package techit.gongsimchae.domain.web.common;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
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

        List<ItemCardResDtoWeb> popularItems = itemService.getPopularItems();
        model.addAttribute("popularItems", popularItems);

        return "home";
    }

    /**
     * 최신아이템 무한스크롤
     */
    @GetMapping("/product/recent")
    public ResponseEntity<?> getRecentItems(@PageableDefault(size = 8) Pageable pageable){
        Page<ItemCardResDtoWeb> recentItems = itemService.getRecentItems(pageable);
        return ResponseEntity.ok().body(recentItems);
    }

}
