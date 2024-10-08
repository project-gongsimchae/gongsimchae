package techit.gongsimchae.domain.web.portion;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import techit.gongsimchae.domain.common.es.service.SubElasticService;
import techit.gongsimchae.domain.portion.areas.entity.MyeondongeupArea;
import techit.gongsimchae.domain.portion.areas.entity.SidoArea;
import techit.gongsimchae.domain.portion.areas.entity.SigunguArea;
import techit.gongsimchae.domain.portion.areas.service.MyeondongeupAreaService;
import techit.gongsimchae.domain.portion.areas.service.SidoAreaService;
import techit.gongsimchae.domain.portion.areas.service.SigunguAreaService;
import techit.gongsimchae.domain.portion.notificationkeyword.dto.NotiKeywordCreateDtoWeb;
import techit.gongsimchae.domain.portion.notificationkeyword.service.NotiKeywordService;
import techit.gongsimchae.domain.portion.subdivision.dto.SubSearchDto;
import techit.gongsimchae.domain.portion.subdivision.dto.SubdivisionRespDto;
import techit.gongsimchae.domain.portion.subdivision.service.SubdivisionService;
import techit.gongsimchae.domain.portion.subdivision.service.ViewCountService;
import techit.gongsimchae.global.dto.PrincipalDetails;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class PortionMainController {


    private final SidoAreaService sidoAreaService;
    private final SigunguAreaService sigunguAreaService;
    private final MyeondongeupAreaService myeondongeupAreaService;
    private final SubdivisionService subdivisionService;
    private final NotiKeywordService notiKeywordService;
    private final ViewCountService viewCountService;
    private final SubElasticService subElasticService;




    /**
     * 소분 메인 페이지
     */
    @GetMapping("/portioning")
    public String showPortionPage(@ModelAttribute SubSearchDto searchDto, Model model
            , @PageableDefault(size = 12) Pageable pageable) {


        List<SidoArea> sidoAreas = sidoAreaService.getAllSidoAreas();
        model.addAttribute("sidoAreas", sidoAreas);

        Page<SubdivisionRespDto> subdivisions = null;

        if (searchDto.getType() != null) {
            subdivisions = subElasticService.getRelatedSearchTerms(searchDto.getType());
        } else{
            subdivisions = subdivisionService.getAllSubdivisions(searchDto, pageable);
        }
        viewCountService.setSubdivisionViews(subdivisions.getContent());
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

    /**
     * 키워드 등록
     */

    @GetMapping("/portioning/keywords/create")
    public String createKeywordForm(@ModelAttribute("keyword") NotiKeywordCreateDtoWeb dtoWeb,
                                    @AuthenticationPrincipal PrincipalDetails principalDetails, Model model) throws JsonProcessingException {
        List<String> whiteList = notiKeywordService.getNotificationKeywords(principalDetails);
        model.addAttribute("whiteList", new ObjectMapper().writeValueAsString(whiteList));
        return "portion/keyword";
    }

    @PostMapping("/portioning/keywords/create")
    @ResponseBody
    public ResponseEntity<?> createKeyword(@RequestParam("keyword") String keyword,
                                        @AuthenticationPrincipal PrincipalDetails principalDetails) {
        notiKeywordService.createKeyword(keyword,principalDetails);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/portioning/keywords/delete")
    public ResponseEntity<?> deleteKeyword(@RequestParam("keyword") String keyword,
                                           @AuthenticationPrincipal PrincipalDetails principalDetails) {
        notiKeywordService.deleteNotificationKeyword(keyword,principalDetails);
        return ResponseEntity.ok().build();
    }


}