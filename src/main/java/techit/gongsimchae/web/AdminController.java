package techit.gongsimchae.web;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import techit.gongsimchae.domain.common.user.dto.UserAdminUpdateReqDtoWeb;
import techit.gongsimchae.domain.common.user.dto.UserRespDtoWeb;
import techit.gongsimchae.domain.common.user.service.UserService;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
@Slf4j
public class AdminController {
    private final UserService userService;


    @GetMapping
    public String adminDashboard() {
        return "admin/admin";
    }

    @GetMapping("/users")
    public String usersDashboard(Model model) {
        List<UserRespDtoWeb> users = userService.getUsers();
        model.addAttribute("users", users);
        return "admin/users/userList";
    }

    /**
     * 관리자가 보는 유저정보들
     */
    @GetMapping("/users/{id}")
    public String detailsUser(@PathVariable("id") Long id, Model model) {
        UserRespDtoWeb user = userService.getUser(id);
        model.addAttribute("user", user);
        return "admin/users/userDetail";
    }

    @GetMapping("/users/update/{id}")
    public String userUpdateForm(@PathVariable("id") Long id, Model model) {
        UserRespDtoWeb user = userService.getUser(id);
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
        rttr.addAttribute("id",id);

        return "redirect:/admin/users/{id}";
    }

    @PostMapping("/users/delete/{id}")
    public String userDelete(@PathVariable("id") Long id) {
        userService.deleteUser(id);
        return "redirect:/admin/users";
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






}
