package com.huizhuang.zxsq.http.bean.supervision;

/**
 * 整改
 * 
 * @author admin
 * 
 */
public class NodeEdit {

    private String stage_id;
    private String node_id;
    private int status;
    private String total_score;
    private String last_time;

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

    public String getTotal_score() {
        return total_score;
    }

    public void setTotal_score(String total_score) {
        this.total_score = total_score;
    }

    public String getLast_time() {
        return last_time;
    }

    public void setLast_time(String last_time) {
        this.last_time = last_time;
    }

    @Override
    public String toString() {
        return "NodeEdit [stage_id=" + stage_id + ", node_id=" + node_id + ", status=" + status
                + ", total_score=" + total_score + ", last_time=" + last_time + "]";
    }


}
