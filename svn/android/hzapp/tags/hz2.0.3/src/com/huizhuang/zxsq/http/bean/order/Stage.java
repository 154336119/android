package com.huizhuang.zxsq.http.bean.order;

import java.io.Serializable;

/**
 * 订单列表装修阶段信息
 * 
 * @author th
 * 
 */
public class Stage implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String stage_id;
    private String node_id;
    private int status;
    private int cost_changed;

    public String getStage_id() {
        return stage_id;
    }

    public void setStage_id(String stage_id) {
        this.stage_id = stage_id;
    }

    public String getNode_id() {
        return node_id;
    }

    public void setNode_id(String node_id) {
        this.node_id = node_id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getCost_changed() {
        return cost_changed;
    }

    public void setCost_changed(int cost_changed) {
        this.cost_changed = cost_changed;
    }

    @Override
    public String toString() {
        return "Stage [stage_id=" + stage_id + ", node_id=" + node_id + ", status=" + status
                + ", cost_changed=" + cost_changed + "]";
    }


}
