package techit.gongsimchae.domain.groupbuying.coupon.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import techit.gongsimchae.domain.common.imagefile.entity.ImageFile;
import techit.gongsimchae.domain.common.imagefile.service.ImageS3Service;
import techit.gongsimchae.domain.common.user.entity.User;
import techit.gongsimchae.domain.common.user.repository.UserRepository;
import techit.gongsimchae.domain.groupbuying.coupon.dto.CouponCreateReqDtoWeb;
import techit.gongsimchae.domain.groupbuying.coupon.dto.CouponRespDtoWeb;
import techit.gongsimchae.domain.groupbuying.coupon.entity.Coupon;
import techit.gongsimchae.domain.groupbuying.coupon.repository.CouponRepository;
import techit.gongsimchae.global.dto.PrincipalDetails;
import techit.gongsimchae.global.exception.CustomWebException;


import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CouponService {
    private final CouponRepository couponRepository;
    private final UserRepository userRepository;
    private final ImageS3Service imageS3Service;

    public void saveCoupon(CouponCreateReqDtoWeb createDto) {
        ImageFile imageFile = imageS3Service.storeFile(createDto.getEventBannerImage(), "gongsimchae/event-banner/");
        Coupon coupon = new Coupon(createDto, imageFile);
        couponRepository.save(coupon);
    }
    public List<CouponRespDtoWeb> getAllCoupons(){
        return couponRepository.findAll().stream().map(CouponRespDtoWeb::new).collect(Collectors.toList());
    }

    public void deleteCoupon(Long id) {
        couponRepository.deleteById(id);
    }

    public boolean isValidCode(String couponCode) {
        return couponRepository.findByCouponCode(couponCode).isPresent();
    }

    public List<CouponRespDtoWeb> getUserCoupons(PrincipalDetails principalDetails) {
        User user = userRepository.findByLoginId(principalDetails.getUsername()).orElseThrow(() -> new CustomWebException("not found user"));
        return couponRepository.findCouponsByUserId(user.getId());

    }
}