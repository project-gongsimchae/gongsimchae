package techit.gongsimchae.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import techit.gongsimchae.domain.portion.subdivision.dto.SubdivisionReqDto;
import techit.gongsimchae.domain.portion.subdivision.dto.SubdivisionRespDto;
import techit.gongsimchae.domain.portion.subdivision.service.SubdivisionService;
import techit.gongsimchae.global.dto.PrincipalDetails;

@Slf4j
@Controller
@RequiredArgsConstructor
public class SubdivisionController {

    private final SubdivisionService subdivisionService;

    @GetMapping("/portioning/write")
    public String subdivisionRegisterForm(Model model) {
        model.addAttribute("subdivisionReqDto", new SubdivisionReqDto());

        return "portion/subdivisionRegister";
    }

    @PostMapping("/portioning/write")
    public String subdivisionRegister(@ModelAttribute("subdivisionReqDto") SubdivisionReqDto subdivisionReqDto,
                                      @AuthenticationPrincipal PrincipalDetails principalDetails) {

        log.info("subdivisionRequestDto: {}", subdivisionReqDto);
        String UID = subdivisionService.saveSubdivision(subdivisionReqDto, principalDetails.getAccountDto().getId());

        return "redirect:/portioning/"+ UID;
    }

    @GetMapping("/portioning/{UID}")
    public String subdivisionDetail(@PathVariable("UID") String UID,
                                     Model model) {

        SubdivisionRespDto subdivisionRespDto = subdivisionService.findSubdivisionByUID(UID);

        model.addAttribute("subdivisionRespDto", subdivisionRespDto);

        return "portion/subdivisionDetail";
    }

    @GetMapping("/portioning/{UID}/join")
    public String subdivisionJoinChatRoom(@PathVariable("UID") String UID) {

        

        return "portion/chattingRoom";
    }

}
