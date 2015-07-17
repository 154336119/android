package com.huizhuang.zxsq.http.bean.supervision;

/**
 * 节点是否有投诉
 * 
 * @author th
 * 
 */
public class ComplaintsData {

    private int stauts;
    private ComplaintsInfo datas;

    public int getStauts() {
        return stauts;
    }

    public void setStauts(int stauts) {
        this.stauts = stauts;
    }

    public ComplaintsInfo getDatas() {
        return datas;
    }

    public void setDatas(ComplaintsInfo datas) {
        this.datas = datas;
    }

    @Override
    public String toString() {
        return "ComplaintsData [status=" + stauts + ", datas=" + datas + "]";
    }

}
