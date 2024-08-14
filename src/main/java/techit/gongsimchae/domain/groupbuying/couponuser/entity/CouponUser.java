package techit.gongsimchae.domain.groupbuying.couponuser.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import techit.gongsimchae.domain.groupbuying.coupon.entity.Coupon;
import techit.gongsimchae.domain.common.user.entity.User;

@Entity
@Getter
@NoArgsConstructor
public class CouponUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "coupon_user_id")
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_id")
    private Coupon coupon;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    private int couponStatus; // 0 미사용, 1이 사용된 상태

    public CouponUser(Coupon coupon, User user) {
        this.user = user;
        couponStatus = 0;
        if (coupon != null) addCoupon(coupon);
    }

    /**
     * 편의 메서드
     */

    public void addCoupon(Coupon coupon) {
        this.coupon = coupon;
    }

    public void setStatusDeleted() {
        this.couponStatus = 1;
    }
}
