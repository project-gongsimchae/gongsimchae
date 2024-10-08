package techit.gongsimchae.domain.groupbuying.coupon.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import techit.gongsimchae.domain.common.user.entity.User;
import techit.gongsimchae.domain.common.user.repository.UserRepository;
import techit.gongsimchae.domain.groupbuying.category.entity.Category;
import techit.gongsimchae.domain.groupbuying.category.repository.CategoryRepository;
import techit.gongsimchae.domain.groupbuying.coupon.dto.CouponRespDtoWeb;
import techit.gongsimchae.domain.groupbuying.coupon.entity.Coupon;
import techit.gongsimchae.domain.groupbuying.coupon.repository.CouponRepository;
import techit.gongsimchae.domain.groupbuying.couponcategory.entity.CouponCategory;
import techit.gongsimchae.domain.groupbuying.couponcategory.repository.CouponCategoryRepository;
import techit.gongsimchae.domain.groupbuying.couponuser.entity.CouponUser;
import techit.gongsimchae.domain.groupbuying.couponuser.repository.CouponUserRepository;
import techit.gongsimchae.domain.groupbuying.event.dto.EventCreateReqDtoWeb;
import techit.gongsimchae.domain.groupbuying.event.dto.EventResAdminDtoWeb;
import techit.gongsimchae.domain.groupbuying.event.entity.Event;
import techit.gongsimchae.domain.groupbuying.event.repository.EventRepository;
import techit.gongsimchae.global.dto.PrincipalDetails;
import techit.gongsimchae.global.exception.CustomWebException;
import techit.gongsimchae.global.message.ErrorMessage;

@Service
@RequiredArgsConstructor
public class CouponService {
    private final CouponRepository couponRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final CouponCategoryRepository couponCategoryRepository;
    private final EventRepository eventRepository;
    private final CouponUserRepository couponUserRepository;

    public boolean isValidCode(String couponCode) {
        return couponRepository.findByCouponCode(couponCode).isPresent();
    }

    public List<CouponRespDtoWeb> getUserCoupons(PrincipalDetails principalDetails) {
        User user = userRepository.findByLoginId(principalDetails.getUsername()).orElseThrow(() -> new CustomWebException("not found user"));
        List<CouponRespDtoWeb> couponRespDtoWebs = couponRepository.findCouponsByUserId(user.getId());
        for (CouponRespDtoWeb couponRespDtoWeb : couponRespDtoWebs) {
            List<CouponCategory> couponCategories = couponCategoryRepository.findAllByCouponId(couponRespDtoWeb.getId());
            couponCategories.forEach((couponCategory) ->
                couponRespDtoWeb.addCategories(categoryRepository.findById(couponCategory.getCategory().getId()).orElseThrow(
                        () -> new CustomWebException(ErrorMessage.CATEGORY_NOT_FOUND)
                ).getName())
            );
        }
        return couponRespDtoWebs;
    }

    public void createEventCoupon(EventCreateReqDtoWeb eventDto) {
        Coupon coupon = couponRepository.save(new Coupon(eventDto));
        coupon.connectEvent(eventRepository.findByEventName(eventDto.getEventName()));
        List<Category> categories = categoryRepository.findAllByNameIn(eventDto.getCategoryNames());
        for (Category category : categories) {
            couponCategoryRepository.save(new CouponCategory(coupon, category));
        }
    }

    public List<EventResAdminDtoWeb> getCouponEventInfo(List<Event> events) {
        List<EventResAdminDtoWeb> eventResAdminDtoWebs = new ArrayList<>();
        String couponCode;
        for (Event event : events) {
            Optional<Coupon> target = couponRepository.findByEvent(event);
            couponCode = target.map(Coupon::getCouponCode).orElse("");
            eventResAdminDtoWebs.add(new EventResAdminDtoWeb(event, couponCode));
        }
        return eventResAdminDtoWebs;
    }

    public void deleteCoupon(Long eventId) {
        List<Coupon> coupons = couponRepository.findAllByEventId(eventId);
        coupons.forEach(Coupon::setStatusDeleted);
    }

    public void addCoupon(String couponCode, PrincipalDetails principalDetails) {
        Coupon coupon = couponRepository.findByCouponCode(couponCode).orElseThrow(() -> new CustomWebException("not found coupon"));
        User user = userRepository.findByLoginId(principalDetails.getUsername()).orElseThrow(() -> new CustomWebException("not found user"));
        CouponUser couponUser = new CouponUser(coupon, user);
        couponUserRepository.save(couponUser);

    }
}
