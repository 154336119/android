package com.huizhuang.zxsq.http.bean.order;


/**
 * 取消订单原因
 * 
 * @author th
 * 
 */
public class OrderCancelRason {
    private String label;
    private int code;
    private int sub_options;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getSub_options() {
        return sub_options;
    }

    public void setSub_options(int sub_options) {
        this.sub_options = sub_options;
    }

    @Override
    public String toString() {
        return "OrderCancelRason [label=" + label + ", code=" + code + ", sub_options="
                + sub_options + "]";
    }



}
