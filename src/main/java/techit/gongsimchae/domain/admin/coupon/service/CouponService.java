package techit.gongsimchae.domain.admin.coupon.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import techit.gongsimchae.domain.admin.coupon.entity.Coupon;
import techit.gongsimchae.domain.admin.coupon.repository.CouponRepository;


import java.util.List;

@Service
@RequiredArgsConstructor
public class CouponService {
    private final CouponRepository couponRepository;

    public void saveCoupon(Coupon coupon) {
        couponRepository.save(coupon);
    }
    public List<Coupon> getAllCoupons(){
        return couponRepository.findAll();
    }

    public void deleteCoupon(Long id) {
        couponRepository.deleteById(id);
    }
}