package techit.gongsimchae.web;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import techit.gongsimchae.domain.portion.areas.entity.MyeondongeupArea;
import techit.gongsimchae.domain.portion.areas.entity.SidoArea;
import techit.gongsimchae.domain.portion.areas.entity.SigunguArea;
import techit.gongsimchae.domain.portion.areas.service.MyeondongeupAreaService;
import techit.gongsimchae.domain.portion.areas.service.SidoAreaService;
import techit.gongsimchae.domain.portion.areas.service.SigunguAreaService;
import techit.gongsimchae.domain.portion.subdivision.dto.SubdivisionRespDto;
import techit.gongsimchae.domain.portion.subdivision.service.SubdivisionService;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class PortionMainController {

    private final SidoAreaService sidoAreaService;
    private final SigunguAreaService sigunguAreaService;
    private final MyeondongeupAreaService myeondongeupAreaService;
    private final SubdivisionService subdivisionService;


    /**
     * 소분 메인 페이지
     */
    @GetMapping("/portioning")
    public String showPortionPage(Model model) {
        List<SidoArea> sidoAreas = sidoAreaService.getAllSidoAreas();
        model.addAttribute("sidoAreas", sidoAreas);

        List<SubdivisionRespDto> subdivisions = subdivisionService.getAllSubdivisions();
        model.addAttribute("subdivisions", subdivisions);

        return "portion/portioningMain";
    }

    @GetMapping("/sigungus")
    public String getSigunguAreas(@RequestParam Long sidoAreaId, Model model){
        List<SigunguArea> sigunguAreas = sigunguAreaService.getSigunguAreasBySidoAreaId(sidoAreaId);
        model.addAttribute("sigunguAreas", sigunguAreas);
        return "portion/sigungus :: sigunguList";
    }

    @GetMapping("/myeondongeups")
    public String getMyeondongeupAreas(@RequestParam Long sigunguAreaId, Model model){
        List<MyeondongeupArea> myeondongeupAreas = myeondongeupAreaService.getMyeondongeupAreasBySigunguAreaId(sigunguAreaId);
        model.addAttribute("myeondongeupAreas", myeondongeupAreas);
        return "portion/myeondongeups :: myeondongeupList";
    }
}