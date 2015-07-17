package com.huizhuang.zxsq.http.bean.coupon;

/**
 * 优惠券类类型明细
 * @author th
 *
 */
public class CouponDetail {

    private String amount;
    private String day;
    private String remark;

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toString() {
        return "CouponDetail [amount=" + amount + ", day=" + day + ", remark=" + remark + "]";
    }


}
