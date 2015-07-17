package com.huizhuang.zxsq.http.bean.supervision;

public class CheckInfoDetail {

    private String demo_id;
    private String demo_title;
    private String demo_remark;
    private String demo_img_path;
    private String demo_thumb_path;
    private String scene_iid;
    private String scene_img_path;
    private String scene_thumb_path;
    private float scene_score;

    public String getDemo_id() {
        return demo_id;
    }

    public void setDemo_id(String demo_id) {
        this.demo_id = demo_id;
    }

    public String getDemo_title() {
        return demo_title;
    }

    public void setDemo_title(String demo_title) {
        this.demo_title = demo_title;
    }

    public String getDemo_remark() {
        return demo_remark;
    }

    public void setDemo_remark(String demo_remark) {
        this.demo_remark = demo_remark;
    }

    public String getDemo_img_path() {
        return demo_img_path;
    }

    public void setDemo_img_path(String demo_img_path) {
        this.demo_img_path = demo_img_path;
    }

    public String getDemo_thumb_path() {
        return demo_thumb_path;
    }

    public void setDemo_thumb_path(String demo_thumb_path) {
        this.demo_thumb_path = demo_thumb_path;
    }

    public String getScene_iid() {
        return scene_iid;
    }

    public void setScene_iid(String scene_iid) {
        this.scene_iid = scene_iid;
    }

    public String getScene_img_path() {
        return scene_img_path;
    }

    public void setScene_img_path(String scene_img_path) {
        this.scene_img_path = scene_img_path;
    }

    public String getScene_thumb_path() {
        return scene_thumb_path;
    }

    public void setScene_thumb_path(String scene_thumb_path) {
        this.scene_thumb_path = scene_thumb_path;
    }
    
    public float getScene_score() {
        return scene_score;
    }

    public void setScene_score(float scene_score) {
        this.scene_score = scene_score;
    }

    @Override
    public String toString() {
        return "CheckInfoDetail [demo_id=" + demo_id + ", demo_title=" + demo_title
                + ", demo_remark=" + demo_remark + ", demo_img_path=" + demo_img_path
                + ", demo_thumb_path=" + demo_thumb_path + ", scene_iid=" + scene_iid
                + ", scene_img_path=" + scene_img_path + ", scene_thumb_path=" + scene_thumb_path
                + ", scene_score=" + scene_score + "]";
    }

}
