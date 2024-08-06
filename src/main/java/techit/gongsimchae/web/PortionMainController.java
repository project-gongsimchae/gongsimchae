package techit.gongsimchae.web;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import techit.gongsimchae.domain.portion.areas.entity.MyeondongeupArea;
import techit.gongsimchae.domain.portion.areas.entity.SidoArea;
import techit.gongsimchae.domain.portion.areas.entity.SigunguArea;
import techit.gongsimchae.domain.portion.areas.repository.MyeondongeupAreaRepository;
import techit.gongsimchae.domain.portion.areas.repository.SidoAreaRepository;
import techit.gongsimchae.domain.portion.areas.repository.SigunguAreaRepository;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class PortionMainController {

    private final SidoAreaRepository sidoAreaRepository;
    private final SigunguAreaRepository sigunguAreaRepository;
    private final MyeondongeupAreaRepository myeondongeupAreaRepository;

    /**
     * 소분 메인 페이지
     */
    @GetMapping("/portioning")
    public String showPortionPage(Model model) {
        List<SidoArea> sidoAreas = sidoAreaRepository.findAll();
        model.addAttribute("sidoAreas", sidoAreas);


        return "portion/portioningMain";
    }

    @GetMapping("/sigungus")
    public String getSigunguAreas(@RequestParam Long sidoAreaId, Model model){
        List<SigunguArea> sigunguAreas = sigunguAreaRepository.findBySidoAreaId(sidoAreaId);
        model.addAttribute("sigunguAreas", sigunguAreas);
        return "portion/sigungus :: sigunguList";
    }

    @GetMapping("/myeondongeups")
    public String getMyeondongeupAreas(@RequestParam Long sigunguAreaId, Model model){
        List<MyeondongeupArea> myeondongeupAreas = myeondongeupAreaRepository.findBySigunguAreaId(sigunguAreaId);
        model.addAttribute("myeondongeupAreas", myeondongeupAreas);
        return "portion/myeondongeups :: myeondongeupList";
    }
}