package techit.gongsimchae.domain.portion.notifications.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import techit.gongsimchae.domain.common.user.entity.User;
import techit.gongsimchae.domain.common.user.repository.UserRepository;
import techit.gongsimchae.domain.portion.notifications.repository.NotificationRepository;
import techit.gongsimchae.global.dto.PrincipalDetails;

@Component
@RequiredArgsConstructor
@Slf4j
public class NotificationInterceptor implements HandlerInterceptor {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        Authentication authentication = SecurityContextHolder.getContextHolderStrategy().getContext().getAuthentication();
        log.debug("interceptor authentication {}", authentication);
        if (modelAndView != null && !isRedirectView(modelAndView) && authentication != null && authentication.getPrincipal() instanceof PrincipalDetails) {
            User user = userRepository.findByLoginId(((PrincipalDetails) authentication.getPrincipal()).getUsername()).orElseThrow(RuntimeException::new);
            Long count = notificationRepository.countByUserChecked(user.getId(), 0);
            log.debug("notification count: {}", count);
            if (count > 0) {
                modelAndView.addObject("hasNotification", count);
            }

        }
    }

    private boolean isRedirectView(ModelAndView modelAndView) {
        return modelAndView.getViewName().startsWith("redirect:") || modelAndView.getView() instanceof RedirectView;
    }
}
