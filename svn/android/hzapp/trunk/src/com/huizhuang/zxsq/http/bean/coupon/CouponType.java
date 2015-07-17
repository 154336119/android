package com.huizhuang.zxsq.http.bean.coupon;

/**
 * 优惠券类型
 * @author th
 *
 */
public class CouponType {

    private CouponDetail new_user_down;
    private CouponDetail order_share;
    private CouponDetail order_done_share;

    public CouponDetail getNew_user_down() {
        return new_user_down;
    }

    public void setNew_user_down(CouponDetail new_user_down) {
        this.new_user_down = new_user_down;
    }

    public CouponDetail getOrder_share() {
        return order_share;
    }

    public void setOrder_share(CouponDetail order_share) {
        this.order_share = order_share;
    }

    public CouponDetail getOrder_done_share() {
        return order_done_share;
    }

    public void setOrder_done_share(CouponDetail order_done_share) {
        this.order_done_share = order_done_share;
    }

    @Override
    public String toString() {
        return "CouponType [new_user_down=" + new_user_down + ", order_share=" + order_share
                + ", order_done_share=" + order_done_share + "]";
    }

}
