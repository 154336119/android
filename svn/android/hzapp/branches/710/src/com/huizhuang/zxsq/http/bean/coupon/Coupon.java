package com.huizhuang.zxsq.http.bean.coupon;

/**
 * 添加优惠券
 * @author th
 *
 */
public class Coupon {

    private int statu;
    private String amount;

    public int getStatu() {
        return statu;
    }

    public void setStatu(int statu) {
        this.statu = statu;
    }


    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "Coupon [statu=" + statu + ", amount=" + amount + "]";
    }



}
