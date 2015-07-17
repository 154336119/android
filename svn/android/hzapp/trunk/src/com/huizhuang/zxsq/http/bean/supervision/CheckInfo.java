package com.huizhuang.zxsq.http.bean.supervision;

import java.util.List;

import com.huizhuang.zxsq.http.bean.common.Image;

/**
 * 各阶段验收信息
 * 
 * @author th
 * 
 */
public class CheckInfo {

    private String stage_id;
    private int node_id;
    private int status;
    private Float total_score;
    private String last_time;
    private int cost;
    private int delay;
    private List<Image> a;
    private List<Image> b;
    private List<Image> statement_list;
    private List<Image> warranty_list;
    private List<CheckInfoDetail> info;

    public String getStage_id() {
        return stage_id;
    }

    public void setStage_id(String stage_id) {
        this.stage_id = stage_id;
    }

    public int getNode_id() {
        return node_id;
    }

    public void setNode_id(int node_id) {
        this.node_id = node_id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Float getTotal_score() {
        return total_score;
    }

    public void setTotal_score(Float total_score) {
        this.total_score = total_score;
    }

    public String getLast_time() {
        return last_time;
    }

    public void setLast_time(String last_time) {
        this.last_time = last_time;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public int getDelay() {
        return delay;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

    public List<Image> getA() {
        return a;
    }

    public void setA(List<Image> a) {
        this.a = a;
    }

    public List<Image> getB() {
        return b;
    }

    public void setB(List<Image> b) {
        this.b = b;
    }

    public List<Image> getStatement_list() {
        return statement_list;
    }

    public void setStatement_list(List<Image> statement_list) {
        this.statement_list = statement_list;
    }

    public List<Image> getWarranty_list() {
        return warranty_list;
    }

    public void setWarranty_list(List<Image> warranty_list) {
        this.warranty_list = warranty_list;
    }

    public List<CheckInfoDetail> getInfo() {
        return info;
    }

    public void setInfo(List<CheckInfoDetail> info) {
        this.info = info;
    }

    @Override
    public String toString() {
        return "CheckInfo [stage_id=" + stage_id + ", node_id=" + node_id + ", status=" + status
                + ", total_score=" + total_score + ", last_time=" + last_time + ", cost=" + cost
                + ", delay=" + delay + ", a=" + a + ", b=" + b + ", statement_list="
                + statement_list + ", warranty_list=" + warranty_list + ", info=" + info + "]";
    }


}
