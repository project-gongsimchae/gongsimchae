package techit.gongsimchae.domain.web.common;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import techit.gongsimchae.domain.portion.notifications.dto.NotificationResponse;
import techit.gongsimchae.domain.portion.notifications.service.NotificationService;
import techit.gongsimchae.global.dto.PrincipalDetails;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@Slf4j
public class NotificationController {
    private final NotificationService notificationService;

    @GetMapping("/api/check-auth")
    @ResponseBody
    public ResponseEntity<Map<String, Boolean>> checkAuth() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isAuthenticated = auth != null && auth.isAuthenticated() && !(auth instanceof AnonymousAuthenticationToken);

        Map<String, Boolean> response = new HashMap<>();
        response.put("isAuthenticated", isAuthenticated);

        return ResponseEntity.ok(response);
    }


    @GetMapping("/mypage/notifications")
    public String notifications(Model model, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        NotificationResponse notificationResponse = notificationService.getAllNotifications(principalDetails);
        model.addAttribute("notificationResponse", notificationResponse);

        return "user/notifications";
    }

    @GetMapping("/mypage/read/notifications")
    public ResponseEntity<?> readNotifications(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        log.debug("read notifications {}" ,principalDetails);
        notificationService.readNotification(principalDetails);
        return ResponseEntity.ok().build();
    }
}
