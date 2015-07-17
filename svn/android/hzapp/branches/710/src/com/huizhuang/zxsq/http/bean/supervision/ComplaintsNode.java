package com.huizhuang.zxsq.http.bean.supervision;

import java.io.Serializable;

/**
 * 投诉节点
 * @author th
 *
 */
public class ComplaintsNode implements Serializable{

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String dispute_node_id;
    private String dispute_node_name;

    public String getDispute_node_id() {
        return dispute_node_id;
    }

    public void setDispute_node_id(String dispute_node_id) {
        this.dispute_node_id = dispute_node_id;
    }

    public String getDispute_node_name() {
        return dispute_node_name;
    }

    public void setDispute_node_name(String dispute_node_name) {
        this.dispute_node_name = dispute_node_name;
    }

    @Override
    public String toString() {
        return "ComplaintsNode [dispute_node_id=" + dispute_node_id + ", dispute_node_name="
                + dispute_node_name + "]";
    }


}
