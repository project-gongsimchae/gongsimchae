package techit.gongsimchae.web;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import techit.gongsimchae.domain.common.address.dto.AddressCreateReqDtoWeb;
import techit.gongsimchae.domain.common.address.service.AddressService;
import techit.gongsimchae.domain.common.inquiry.dto.InquiryAdminReplyReqDtoWeb;
import techit.gongsimchae.domain.common.inquiry.dto.InquiryRespDtoWeb;
import techit.gongsimchae.domain.common.inquiry.service.InquiryService;
import techit.gongsimchae.domain.common.user.dto.UserAdminUpdateReqDtoWeb;
import techit.gongsimchae.domain.common.user.dto.UserRespDtoWeb;
import techit.gongsimchae.domain.common.user.service.UserService;
import techit.gongsimchae.domain.groupbuying.category.dto.CategoryReqDtoWeb;
import techit.gongsimchae.domain.groupbuying.category.entity.Category;
import techit.gongsimchae.domain.groupbuying.category.service.CategoryService;
import techit.gongsimchae.domain.groupbuying.item.service.ItemService;
import techit.gongsimchae.domain.portion.report.dto.ReportRespDtoWeb;
import techit.gongsimchae.domain.portion.report.service.ReportService;
import techit.gongsimchae.domain.portion.subdivision.dto.SubdivisionReportRespDto;
import techit.gongsimchae.domain.portion.subdivision.service.SubdivisionService;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
@Slf4j
public class AdminController {
    private final UserService userService;
    private final InquiryService inquiryService;
    private final AddressService addressService;
    private final SubdivisionService subdivisionService;
    private final ReportService reportService;
    private final CategoryService categoryService;
    private final ItemService itemService;


    @GetMapping
    public String adminDashboard() {
        return "admin/admin";
    }

    @GetMapping("/users")
    public String usersDashboard(Model model, @PageableDefault(size = 10, sort = "createDate") Pageable pageable) {
        Page<UserRespDtoWeb> users = userService.getUsers(pageable);
        model.addAttribute("users", users);
        return "admin/users/userList";
    }

    /**
     * 관리자가 보는 유저정보들
     */
    @GetMapping("/users/{id}")
    public String detailsUser(@PathVariable("id") Long id, Model model) {
        UserRespDtoWeb user = userService.getUser(id);
        user.setAddress(addressService.getDefaultAddress(id));
        model.addAttribute("user", user);
        return "admin/users/userDetail";
    }

    @GetMapping("/users/update/{id}")
    public String userUpdateForm(@PathVariable("id") Long id, Model model) {
        UserRespDtoWeb user = userService.getUser(id);
        user.setAddress(addressService.getDefaultAddress(id));

        model.addAttribute("user", user);
        return "admin/users/userUpdate";
    }

    @PostMapping("/users/update/{id}")
    public String userUpdate(@PathVariable("id") Long id, @Valid @ModelAttribute("user")UserAdminUpdateReqDtoWeb user
            , BindingResult bindingResult, RedirectAttributes rttr) {
        if(bindingResult.hasErrors()) {
            log.info("admin user update error {}" , bindingResult.getAllErrors());
            return "admin/users/userUpdate";
        }

        userService.updateByAdmin(user);
        if(user.getAddress() != null)
            addressService.addAddress(new AddressCreateReqDtoWeb(user),user.getId());

        rttr.addAttribute("id",id);

        return "redirect:/admin/users/{id}";
    }

    @PostMapping("/users/delete/{id}")
    public String userDelete(@PathVariable("id") Long id) {
        userService.deleteUser(id);
        return "redirect:/admin/users";
    }

    /**
     * 관리자 카테고리
     */
    @GetMapping("/category")
    public String categoryDashBoard(Model model, @PageableDefault(size = 10, sort = "id") Pageable pageable) {
        Page<Category> categories = categoryService.getCategories(pageable);
        model.addAttribute("categoryList", categories);
        return "/admin/category/categoryList";
    }

    /**
     * 카테고리 생성
     */
    @PostMapping("/category")
    public String createCategory(CategoryReqDtoWeb categoryReqDtoWeb){
        categoryService.createCategory(categoryReqDtoWeb);
        return "redirect:/admin/category";
    }

    /**
     * 카테고리 삭제
     * 데이터 삭제 x, status를 1로 변경
     */
    @PostMapping("/category/delete")
    public String deleteCategory(CategoryReqDtoWeb categoryDtoWeb){
        categoryService.deleteCategory(categoryDtoWeb);
        return "redirect:/admin/category";
    }

    /**
     * 관리자 배너
     */
    @GetMapping("/banners")
    public String banners(){
        return "admin/banners/bannerList";
    }

    @GetMapping("/banners/write")
    public String bannerForm(){
        return "admin/banners/write";
    }

    @PostMapping("/banners/write")
    public String banner(){
        return "redirect:/admin/banners";
    }


    /**
     * 유저가 쓴 1:1 문의를 관리자가 보고 작성
     */
    @GetMapping("/inquires")
    public String InquiryList(Model model, @RequestParam(value = "filter", defaultValue = "unanswered") String filter, @PageableDefault(size = 5) Pageable pageable) {
        Page<InquiryRespDtoWeb> inquires = inquiryService.getAllInquiries(pageable, filter);
        log.debug("admin inquires {}", inquires.getContent());
        model.addAttribute("inquires", inquires);
        return "admin/inquiry/inquiryList";
    }

    @GetMapping("/inquires/reply/{id}")
    public String ReplyToInquiryForm(@PathVariable("id") String id, Model model) {
        InquiryRespDtoWeb inquiry = inquiryService.getInquiry(id);
        log.debug("inquiry reply {} ", inquiry);
        model.addAttribute("inquiry", inquiry);
        return "admin/inquiry/inquiryReply";
    }

    @PostMapping("/inquires/reply/{id}")
    public String ReplyToInquiry(@PathVariable("id") String id
            , @Valid @ModelAttribute("inquiry")InquiryAdminReplyReqDtoWeb dtoWeb, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            log.debug("inquiry error {}", bindingResult.getAllErrors());
            return "admin/inquiry/inquiryReply";
        }
        inquiryService.replyToInquiry(id, dtoWeb);
        return "redirect:/admin/inquires";
    }

    @PostMapping("/inquires/delete/{id}")
    public String DeleteInquiry(@PathVariable("id") String id) {
        inquiryService.deleteInquiry(id);
        return "redirect:/admin/inquires";
    }

    /**
     * 신고수가 제일 많은 신고글 보기
     */
    @GetMapping("/reports")
    public String ReportList(Model model, @PageableDefault(size = 3) Pageable pageable) {
        Page<SubdivisionReportRespDto> reports = subdivisionService.getMostReported(pageable);
        model.addAttribute("reports", reports);
        return "admin/reports/reportList";
    }

    @GetMapping("/reports/{id}")
    public String SubdivisionReport(@PathVariable("id") Long id, Model model, @PageableDefault(size = 10) Pageable pageable) {
        Page<ReportRespDtoWeb> reports = reportService.getSubdivisionReport(id, pageable);
        model.addAttribute("reports", reports);
        return "admin/reports/subdivisionReport";
    }

    /**
     * 신고 초기화
     */
    @PostMapping("/reports/reset/{subdivisionUID}")
    public String restReport(@PathVariable("subdivisionUID") String subdivisionUID) {
        reportService.deleteAllReport(subdivisionUID);
        return "redirect:/admin/reports";
    }


    /**
     * 글 삭제
     */
    @PostMapping("/subdivision/delete/{subdivisionId}")
    public String deleteSubdivision(@PathVariable("subdivisionId") String subdivisionId) {
        subdivisionService.deleteSubdivision(subdivisionId);
        return "redirect:/admin/reports";
    }

    /**
     * 유저 제재
     */

    @PostMapping("/users/ban/{userId}")
    public String deleteSubdivision(@PathVariable("userId") Long userId) {
        userService.banUser(userId);
        return "redirect:/admin/reports";
    }






}
