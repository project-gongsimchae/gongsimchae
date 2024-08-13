package techit.gongsimchae.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import techit.gongsimchae.domain.portion.subdivision.dto.SubdivisionReqDto;
import techit.gongsimchae.domain.portion.subdivision.dto.SubdivisionRespDto;
import techit.gongsimchae.domain.portion.subdivision.dto.SubdivisionUpdateReqDto;
import techit.gongsimchae.domain.portion.subdivision.service.SubdivisionService;
import techit.gongsimchae.global.dto.PrincipalDetails;

@Slf4j
@Controller
@RequestMapping("/portioning")
@RequiredArgsConstructor
public class SubdivisionController {

    private final SubdivisionService subdivisionService;

    @GetMapping("/write")
    public String subdivisionRegisterForm(Model model) {
        model.addAttribute("subdivisionReqDto", new SubdivisionReqDto());

        return "portion/subdivisionRegister";
    }

    @PostMapping("/write")
    public String subdivisionRegister(@ModelAttribute("subdivisionReqDto") SubdivisionReqDto subdivisionReqDto,
                                      @AuthenticationPrincipal PrincipalDetails principalDetails) {

        log.info("subdivisionRequestDto: {}", subdivisionReqDto);
        String UID = subdivisionService.saveSubdivision(subdivisionReqDto, principalDetails.getAccountDto().getId());

        return "redirect:/portioning/"+ UID;
    }

    @GetMapping("/{UID}")
    public String subdivisionDetail(@PathVariable("UID") String UID,
                                     Model model) {

        SubdivisionRespDto subdivisionRespDto = subdivisionService.findSubdivisionByUID(UID);

        model.addAttribute("subdivisionRespDto", subdivisionRespDto);

        return "portion/subdivisionDetail";
    }

    @GetMapping("/{UID}/join")
    public String subdivisionJoinChatRoom(@PathVariable("UID") String UID) {

        return "portion/chattingRoom";
    }

    @GetMapping("/{UID}/update")
    public String subdivisionUpdateForm(@PathVariable("UID") String UID, Model model,
                                        @AuthenticationPrincipal PrincipalDetails principalDetails) {

        SubdivisionRespDto subdivisionRespDto = subdivisionService.findSubdivisionByUID(UID);

        if (principalDetails == null || principalDetails.getAccountDto().getId() != subdivisionRespDto.getUser().getId()) {
            return "redirect:/portioning";
        }

        model.addAttribute("subdivisionRespDto", subdivisionRespDto);

        return "portion/subdivisionUpdate";
    }

    @PostMapping("/update")
    public String subdivisionUpdate(@ModelAttribute("subdivisionUpdateReqDto") SubdivisionUpdateReqDto subdivisionUpdateReqDto) {

        String UID = subdivisionService.updateSubdivision(subdivisionUpdateReqDto);

        return "redirect:/portioning/"+UID;
    }

}
