package techit.gongsimchae.web;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import techit.gongsimchae.domain.portion.subdivision.dto.SubdivisionDto;
import techit.gongsimchae.domain.portion.subdivision.service.SubdivisionService;

@Controller
@RequiredArgsConstructor
public class SubdivisionController {

    private final SubdivisionService subdivisionService;

    @GetMapping("/portioning/write")
    public String subdivisionRegisterForm() {

        return "/portion/subdivisionRegister";
    }

    @PostMapping("/portioning/write")
    public String subdivisionRegister() {

        return null;
    }

    @GetMapping("/portioning/{UID}")
    public String subdivisionDetail(@PathVariable("UID") String UID,
                                     Model model) {

        SubdivisionDto subdivisionDto = subdivisionService.findSubdivisionByUID(UID);

        model.addAttribute("subdivisionDto", subdivisionDto);

        return "/portion/subdivisionDetail";
    }

}
