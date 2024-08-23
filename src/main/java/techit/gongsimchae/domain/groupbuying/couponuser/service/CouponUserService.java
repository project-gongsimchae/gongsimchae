package techit.gongsimchae.domain.groupbuying.couponuser.service;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import techit.gongsimchae.domain.common.user.entity.User;
import techit.gongsimchae.domain.common.user.repository.UserRepository;
import techit.gongsimchae.domain.groupbuying.coupon.entity.Coupon;
import techit.gongsimchae.domain.groupbuying.coupon.repository.CouponRepository;
import techit.gongsimchae.domain.groupbuying.couponuser.entity.CouponUser;
import techit.gongsimchae.domain.groupbuying.couponuser.repository.CouponUserRepository;
import techit.gongsimchae.global.dto.AccountDto;
import techit.gongsimchae.global.exception.CustomWebException;
import techit.gongsimchae.global.message.ErrorMessage;
import techit.gongsimchae.global.message.SuccessMessage;

@Service
@Transactional
@RequiredArgsConstructor
public class CouponUserService {

    private final CouponRepository couponRepository;
    private final CouponUserRepository couponUserRepository;
    private final UserRepository userRepository;

    public void deleteCouponUser(Long eventId){
        List<Coupon> coupons = couponRepository.findAllByEventId(eventId);
        List<CouponUser> couponUsers = couponUserRepository.findAllByCouponIn(coupons);
        couponUsers.forEach(CouponUser::setStatusDeleted);
    }

    public ResponseEntity<String> createCouponUser(AccountDto accountDto, Long eventId) {
        Coupon coupon = couponRepository.findByEventId(eventId).orElseThrow(
                () -> new CustomWebException(ErrorMessage.COUPON_NOT_FOUND)
        );
        User user = userRepository.findById(accountDto.getId()).orElseThrow(
                () -> new CustomWebException(ErrorMessage.USER_NOT_FOUND)
        );
        Optional<CouponUser> couponUser = couponUserRepository.findByCouponAndUser(coupon, user);
        if (couponUser.isPresent()){
            return new ResponseEntity<String>(ErrorMessage.COUPON_USER_ALREADY_EXIST,HttpStatus.BAD_REQUEST);
        }
        couponUserRepository.save(new CouponUser(coupon, user));
        return new ResponseEntity<String>(SuccessMessage.ISSUE_COUPON_SUCCESS, HttpStatus.OK);
    }
}
