package techit.gongsimchae.domain.web.portion;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import techit.gongsimchae.domain.common.wishlist.service.WishListService;
import techit.gongsimchae.domain.portion.chatroom.dto.ChatRoomRespDto;
import techit.gongsimchae.domain.portion.chatroom.service.ChatRoomService;
import techit.gongsimchae.domain.portion.feedback.dto.FeedbackReqDtoWeb;
import techit.gongsimchae.domain.portion.feedback.dto.FeedbackUserRespDtoWeb;
import techit.gongsimchae.domain.portion.feedback.service.FeedbackService;
import techit.gongsimchae.domain.portion.report.dto.ReportCreateReqDtoWeb;
import techit.gongsimchae.domain.portion.report.service.ReportService;
import techit.gongsimchae.domain.portion.subdivision.dto.SubdivisionReqDto;
import techit.gongsimchae.domain.portion.subdivision.dto.SubdivisionRespDto;
import techit.gongsimchae.domain.portion.subdivision.dto.SubdivisionUpdateReqDto;
import techit.gongsimchae.domain.portion.subdivision.service.SubdivisionService;
import techit.gongsimchae.domain.portion.subdivision.service.ViewCountService;
import techit.gongsimchae.global.dto.PrincipalDetails;
import techit.gongsimchae.global.util.CookieUtil;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/portioning")
@RequiredArgsConstructor
public class SubdivisionController {

    private final SubdivisionService subdivisionService;
    private final WishListService wishListService;
    private final ChatRoomService chatRoomService;
    private final ReportService reportService;
    private final ViewCountService viewCountService;
    private final FeedbackService feedbackService;

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
                                    @AuthenticationPrincipal PrincipalDetails userDetails,
                                    Model model, HttpServletRequest request, HttpServletResponse response) {

        String viewCookie = CookieUtil.createViewCookie(request, response);
        viewCountService.incrementViewCount(UID, viewCookie);
        Integer viewCount = viewCountService.getSubdivisionViewCount(UID);

        SubdivisionRespDto subdivisionRespDto = subdivisionService.findSubdivisionByUID(UID);
        subdivisionRespDto.setViews(viewCount);

        // subdivision에 있는 chatRoom 가져오기
        ChatRoomRespDto chatRoom = chatRoomService.getChatRoom(subdivisionRespDto.getId());
        model.addAttribute("room", chatRoom);

        model.addAttribute("subdivisionRespDto", subdivisionRespDto);

        boolean isLoggedIn = userDetails != null;

        if(isLoggedIn) {
            Long userId = userDetails.getAccountDto().getId();
            boolean isOwner = wishListService.isOwner(userId, subdivisionRespDto.getUID());
            model.addAttribute("isOwner", isOwner);

            boolean isWishListed = wishListService.isWishListed(userId, subdivisionRespDto.getUID());
            model.addAttribute("isWishListed", isWishListed);
        } else {
            model.addAttribute("isWishListed", false);
            model.addAttribute("isOwner", false);
        }

        return "portion/subdivisionDetail";
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

    @PostMapping("/{UID}/delete")
    public String subdivisionDelete(@PathVariable("UID") String UID) {

        subdivisionService.deleteSubdivision(UID);

        return "redirect:/portioning";
    }

    /**
     * 모집중, 모집완료, 거래완료로 바꿔준다
     */
    @PostMapping("/{UID}/status")
    public String changeSubdivisionStatus(@PathVariable("UID") String UID, @RequestParam("type") String status) {
        subdivisionService.changeStatus(UID, status);
        return "redirect:/portioning/{UID}";

    }

    /**
     * 게시글 신고하기
     **/
    @GetMapping("/report/write")
    public String reportSubdivisionForm(@RequestParam("uid") String uid, Model model, @ModelAttribute("report")ReportCreateReqDtoWeb reportCreateReqDtoWeb) {
        model.addAttribute("uid", uid);
        return "portion/report";
    }

    @PostMapping("/report/write")
    @ResponseBody
    public ResponseEntity<?> reportSubdivision(@Valid @ModelAttribute("report") ReportCreateReqDtoWeb reportCreateReqDtoWeb,
                                    BindingResult bindingResult, @AuthenticationPrincipal PrincipalDetails principalDetails,
                                    RedirectAttributes rttr
                                    ) {
        log.debug("report subdivision request: {}", reportCreateReqDtoWeb);
        if(bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body("신고 내용에 오류가 있습니다.");
        }
        reportService.createReport(reportCreateReqDtoWeb, principalDetails);
        rttr.addAttribute("uid", reportCreateReqDtoWeb.getUid());

        return ResponseEntity.ok("신고가 정상적으로 처리되었습니다.");
    }


    /**
     * 피드백
     */

    @GetMapping("/feedback/write")
    public String feedbackForm(@RequestParam("url") String url, Model model, @AuthenticationPrincipal PrincipalDetails principalDetails,
                               @ModelAttribute("feedback") FeedbackReqDtoWeb feedbackReqDtoWeb) {
        List<FeedbackUserRespDtoWeb> users = feedbackService.findUsersForFeedback(url, principalDetails);
        model.addAttribute("users", users);
        model.addAttribute("url", url);
        return "portion/feedback";

    }

    @PostMapping("/feedback/write")
    @ResponseBody
    public ResponseEntity<?> feedback(@Valid @ModelAttribute("feedback") FeedbackReqDtoWeb reqDtoWeb, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }
        feedbackService.createFeedback(reqDtoWeb);
        return ResponseEntity.ok().build();
    }
}
